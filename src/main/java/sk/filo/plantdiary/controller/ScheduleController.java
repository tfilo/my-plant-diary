package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.filo.plantdiary.service.ScheduleService;
import sk.filo.plantdiary.service.so.CreateScheduleSO;
import sk.filo.plantdiary.service.so.ScheduleSO;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Tag(name = "schedule", description = "Schedule for plant care endpoint")
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleSO> createSchedule(@Valid @NotNull @RequestBody CreateScheduleSO createScheduleSO) {
        LOGGER.debug("create({})", createScheduleSO);
        return new ResponseEntity<>(scheduleService.create(createScheduleSO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ScheduleSO> updateSchedule(@Valid @NotNull @RequestBody ScheduleSO scheduleSO) {
        LOGGER.debug("update({})", scheduleSO);
        return new ResponseEntity<>(scheduleService.update(scheduleSO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleSO> getOneSchedule(@NotNull @PathVariable Long id) {
        LOGGER.debug("getOne({})", id);
        return new ResponseEntity<>(scheduleService.getOne(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ScheduleSO>> getAllSchedules(
            @RequestParam Optional<Long> plantId,
            @NotNull @Min(0) @RequestParam Integer page,
            @NotNull @Min(5) @Max(100) @RequestParam Integer pageSize) {
        LOGGER.debug("getAll()");
        return new ResponseEntity<>(scheduleService.getAllPaginated(plantId, page, pageSize), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteSchedule(@NotNull @PathVariable Long id) {
        LOGGER.debug("delete({})", id);
        scheduleService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
