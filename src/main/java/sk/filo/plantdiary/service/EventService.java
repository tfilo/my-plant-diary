package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.EventType;
import sk.filo.plantdiary.dao.domain.Plant;
import sk.filo.plantdiary.dao.repository.EventRepository;
import sk.filo.plantdiary.dao.repository.EventTypeRepository;
import sk.filo.plantdiary.dao.repository.PlantRepository;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.helper.AuthHelper;
import sk.filo.plantdiary.service.mapper.EventMapper;
import sk.filo.plantdiary.service.mapper.EventTypeMapper;
import sk.filo.plantdiary.service.so.CreateEventSO;
import sk.filo.plantdiary.service.so.EventSO;
import sk.filo.plantdiary.service.so.EventTypeSO;
import sk.filo.plantdiary.service.so.UpdateEventSO;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    private EventRepository eventRepository;

    private EventTypeRepository eventTypeRepository;

    private PlantRepository plantRepository;

    private EventMapper eventMapper;

    private EventTypeMapper eventTypeMapper;

    private ScheduleService scheduleService;

    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Autowired
    public void setEventTypeRepository(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    @Autowired
    public void setPlantRepository(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Autowired
    public void setEventMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    @Autowired
    public void setEventTypeMapper(EventTypeMapper eventTypeMapper) {
        this.eventTypeMapper = eventTypeMapper;
    }

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public EventSO create(CreateEventSO createEventSO) {
        LOGGER.debug("create {}", createEventSO);

        Event event = eventMapper.toBO(createEventSO);

        setEventType(createEventSO.getType().getId(), event);
        setPlant(createEventSO.getPlant().getId(), event);

        LOGGER.debug("create {}", event);
        Event saved = eventRepository.save(event);

        scheduleService.updateScheduleAsync(event.getType(), event.getPlant().getId());

        return eventMapper.toSO(saved);
    }

    public EventSO update(UpdateEventSO updateEventSO) {
        LOGGER.debug("update {}", updateEventSO);

        Event event = eventRepository.findById(updateEventSO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.EVENT_NOT_FOUND.name()));

        EventType oldEventType = event.getType();
        LocalDateTime oldDateTime = updateEventSO.getDateTime();

        if (event.getPlant().getOwner().getUsername().equals(AuthHelper.getUsername())) {
            eventMapper.toBO(updateEventSO, event);

            setEventType(updateEventSO.getType().getId(), event);

            LOGGER.debug("update {}", event);
            Event save = eventRepository.save(event);

            // changed type so need to update old type too
            if (!oldEventType.getId().equals(event.getType().getId())) {
                // event type changed, update old and new eventType to set correct schedules
                scheduleService.updateScheduleAsync(oldEventType, event.getPlant().getId());
                scheduleService.updateScheduleAsync(event.getType(), event.getPlant().getId());
            } else {
                // update this event type but only if time has changed
                if (!oldDateTime.equals(event.getDateTime())) {
                    scheduleService.updateScheduleAsync(event.getType(), event.getPlant().getId());
                }
            }

            return eventMapper.toSO(save);
        } else {
            // when event owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.EVENT_NOT_FOUND.name());
        }
    }

    public EventSO getOne(Long id) {
        LOGGER.debug("getOne {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.EVENT_NOT_FOUND.name()));

        if (event.getPlant().getOwner().getUsername().equals(AuthHelper.getUsername())) {
            LOGGER.debug("getOne {}", event);
            return eventMapper.toSO(event);
        } else {
            // when event owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.EVENT_NOT_FOUND.name());
        }
    }

    public Page<EventSO> getAllByPlantIdPaginated(Long plantId, Integer page, Integer pageSize) {
        LOGGER.debug("getAllByPlantIdPaginated {}", plantId);

        PageRequest pr = PageRequest.of(page, pageSize, Sort.by("dateTime").descending());

        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

        if (!plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }

        return eventRepository.findByPlantId(plantId, pr).map(eventMapper::toSO);
    }

    public void delete(Long id) {
        LOGGER.debug("delete {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.EVENT_NOT_FOUND.name()));

        if (event.getPlant().getOwner().getUsername().equals(AuthHelper.getUsername())) {
            eventRepository.delete(event);
            scheduleService.updateScheduleAsync(event.getType(), event.getPlant().getId());
        } else {
            // when event owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.EVENT_NOT_FOUND.name());
        }
    }

    public List<EventTypeSO> getAllTypes() {
        LOGGER.debug("getAllTypes");
        return eventTypeMapper.toEventTypeSOList(eventTypeRepository.findAll());
    }

    private void setEventType(Long eventTypeId, Event event) {
        LOGGER.debug("setEventType {} {}", eventTypeId, event);
        EventType eventType = eventTypeRepository.findById(eventTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.EVENT_TYPE_NOT_FOUND.name()));

        LOGGER.debug("setEventType {}", eventType);

        event.setType(eventType);
    }

    private void setPlant(Long plantId, Event event) {
        LOGGER.debug("setPlant {} {}", plantId, event);
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

        LOGGER.debug("setPlant {}", plant);

        if (plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            event.setPlant(plant);
        } else { // if username doesn't match, throw exception that plant for this user was not found
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }
    }
}
