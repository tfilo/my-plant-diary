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
import sk.filo.plantdiary.dao.domain.*;
import sk.filo.plantdiary.dao.repository.EventTypeRepository;
import sk.filo.plantdiary.dao.repository.PlantRepository;
import sk.filo.plantdiary.dao.repository.ScheduleRepository;
import sk.filo.plantdiary.dao.repository.UserRepository;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.helper.AuthHelper;
import sk.filo.plantdiary.service.mapper.ScheduleMapper;
import sk.filo.plantdiary.service.so.CreateScheduleSO;
import sk.filo.plantdiary.service.so.ScheduleSO;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    private ScheduleRepository scheduleRepository;

    private EventTypeRepository eventTypeRepository;

    private PlantRepository plantRepository;

    private UserRepository userRepository;

    private ScheduleMapper scheduleMapper;

    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
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
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setScheduleMapper(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    public ScheduleSO create(CreateScheduleSO createScheduleSO) {
        LOGGER.debug("create {}", createScheduleSO);
        Schedule schedule = scheduleMapper.toBO(createScheduleSO);

        User owner = userRepository.findByUsername(AuthHelper.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name()));

        schedule.setOwner(owner);
        setEventType(createScheduleSO.getType().getId(), schedule);
        setPlant(createScheduleSO.getPlant().getId(), schedule);

        LOGGER.debug("create {}", schedule);
        return scheduleMapper.toSO(scheduleRepository.save(schedule));
    }

    public ScheduleSO update(ScheduleSO scheduleSO) {
        LOGGER.debug("update {}", scheduleSO);
        Schedule schedule = scheduleRepository.findById(scheduleSO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.SCHEDULE_NOT_FOUND.name()));

        if (schedule.getPlant().getOwner().getUsername().equals(AuthHelper.getUsername())) {
            scheduleMapper.toBO(scheduleSO, schedule);

            setEventType(scheduleSO.getType().getId(), schedule);
            setPlant(scheduleSO.getPlant().getId(), schedule);

            LOGGER.debug("update {}", schedule);
            return scheduleMapper.toSO(scheduleRepository.save(schedule));
        } else {
            // when event owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.SCHEDULE_NOT_FOUND.name());
        }
    }

    public ScheduleSO getOne(Long id) {
        LOGGER.debug("getOne {}", id);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.SCHEDULE_NOT_FOUND.name()));

        if (schedule.getPlant().getOwner().getUsername().equals(AuthHelper.getUsername())) {
            LOGGER.debug("getOne {}", schedule);
            return scheduleMapper.toSO(schedule);
        } else {
            // when schedule owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.SCHEDULE_NOT_FOUND.name());
        }
    }

    public Page<ScheduleSO> getAllPaginated(Optional<Long> plantId, Integer page, Integer pageSize) {
        LOGGER.debug("getAllPaginated {}", plantId);
        PageRequest pr = PageRequest.of(page, pageSize, Sort.by("next").descending());

        if (plantId.isPresent()) {
            Plant plant = plantRepository.findById(plantId.get())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

            if (!plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
            }
            return scheduleRepository.findByPlantId(plantId.get(), pr).map(scheduleMapper::toSO); // get all only by plantId
        } else {
            return scheduleRepository.findByOwnerUsername(AuthHelper.getUsername(), pr).map(scheduleMapper::toSO); // get all schedules of user
        }
    }

    public void delete(Long id) {
        LOGGER.debug("delete {}", id);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.SCHEDULE_NOT_FOUND.name()));

        if (schedule.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            scheduleRepository.delete(schedule);
        } else {
            // when event owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.SCHEDULE_NOT_FOUND.name());
        }
    }

    private void setEventType(Long eventTypeId, Schedule schedule) {
        LOGGER.debug("setEventType {} {}", eventTypeId, schedule);
        EventType eventType = eventTypeRepository.findById(eventTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.EVENT_TYPE_NOT_FOUND.name()));

        LOGGER.debug("setEventType {}", eventType);

        schedule.setType(eventType);
    }

    private void setPlant(Long plantId, Schedule schedule) {
        LOGGER.debug("setPlant {} {}", plantId, schedule);
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

        LOGGER.debug("setPlant {}", plant);

        if (plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            schedule.setPlant(plant);
        } else { // if username doesn't match, throw exception that plant for this user was not found
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }
    }
}
