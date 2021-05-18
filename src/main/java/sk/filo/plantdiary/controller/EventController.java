package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.filo.plantdiary.service.EventService;
import sk.filo.plantdiary.service.so.CreateEventSO;
import sk.filo.plantdiary.service.so.EventSO;
import sk.filo.plantdiary.service.so.EventTypeSO;
import sk.filo.plantdiary.service.so.UpdateEventSO;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "event", description = "Plant events endpoint")
@RestController
@RequestMapping("/api/event")
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventSO> createEvent(@Valid @NotNull @RequestBody CreateEventSO createEventSO) {
        LOGGER.debug("create({})", createEventSO);
        return new ResponseEntity<>(eventService.create(createEventSO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<EventSO> updateEvent(@Valid @NotNull @RequestBody UpdateEventSO updateEventSO) {
        LOGGER.debug("update({})", updateEventSO);
        return new ResponseEntity<>(eventService.update(updateEventSO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventSO> getOneEvent(@NotNull @PathVariable Long id) {
        LOGGER.debug("getOne({})", id);
        return new ResponseEntity<>(eventService.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/all/{plantId}")
    public ResponseEntity<Page<EventSO>> getAllEventsByPlantId(
            @NotNull @PathVariable Long plantId,
            @NotNull @Min(0) @RequestParam Integer page,
            @NotNull @Min(5) @Max(100) @RequestParam Integer pageSize) {
        LOGGER.debug("getAllByPlantId({},{},{})", plantId, page, pageSize);

        return new ResponseEntity<>(eventService.getAllByPlantIdPaginated(plantId, page, pageSize), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEvent(@NotNull @PathVariable Long id) {
        LOGGER.debug("delete({})", id);
        eventService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<EventTypeSO>> getAllEventTypes() {
        LOGGER.debug("getAllTypes()");
        return new ResponseEntity<>(eventService.getAllTypes(), HttpStatus.OK);
    }
}
