package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.filo.plantdiary.service.EventService;
import sk.filo.plantdiary.service.so.CreateEventSO;
import sk.filo.plantdiary.service.so.EventSO;
import sk.filo.plantdiary.service.so.EventTypeSO;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "event", description = "Plant events endpoint")
@RestController
@RequestMapping
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/event")
    public ResponseEntity<EventSO> create(@Valid @NotNull @RequestBody CreateEventSO createEventSO) {
        LOGGER.debug("create({})", createEventSO);
        return new ResponseEntity<>(eventService.create(createEventSO), HttpStatus.CREATED);
    }

    @PutMapping("/event")
    public ResponseEntity<EventSO> update(@Valid @NotNull @RequestBody EventSO eventSO) {
        LOGGER.debug("update({})", eventSO);
        return new ResponseEntity<>(eventService.update(eventSO), HttpStatus.OK);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<EventSO> getOne(@NotNull @PathVariable Long id) {
        LOGGER.debug("getOne({})", id);
        return new ResponseEntity<>(eventService.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/event/all/{plantId}")
    public ResponseEntity<Page<EventSO>> getAllByPlantId(
            @NotNull @PathVariable Long plantId,
            @NotNull @Min(0) @RequestParam Integer page,
            @NotNull @Min(5) @Max(100) @RequestParam Integer pageSize) {
        LOGGER.debug("getAllByPlantId({},{},{})", plantId, page, pageSize);

        return new ResponseEntity<>(eventService.getAllByPlantIdPaginated(plantId, page, pageSize), HttpStatus.OK);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {
        LOGGER.debug("delete({})", id);
        eventService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/event/type")
    public ResponseEntity<List<EventTypeSO>> getAllTypes() {
        LOGGER.debug("getAllTypes()");
        return new ResponseEntity<>(eventService.getAllTypes(), HttpStatus.OK);
    }
}
