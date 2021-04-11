package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.filo.plantdiary.service.LocationService;
import sk.filo.plantdiary.service.so.CreateLocationSO;
import sk.filo.plantdiary.service.so.LocationSO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "location", description = "Plant locations endpoint")
@RestController
@RequestMapping("/api")
public class LocationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/location")
    public ResponseEntity<LocationSO> create(@Valid @NotNull @RequestBody CreateLocationSO createLocationSO) {
        LOGGER.debug("create({})", createLocationSO);
        return new ResponseEntity<>(locationService.create(createLocationSO), HttpStatus.CREATED);
    }

    @PutMapping("/location")
    public ResponseEntity<LocationSO> update(@Valid @NotNull @RequestBody LocationSO locationSO) {
        LOGGER.debug("update({})", locationSO);
        return new ResponseEntity<>(locationService.update(locationSO), HttpStatus.OK);
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<LocationSO> getOne(@NotNull @PathVariable Long id) {
        LOGGER.debug("getOne({})", id);
        return new ResponseEntity<>(locationService.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/location")
    public ResponseEntity<List<LocationSO>> getAll() {
        LOGGER.debug("getAll()");
        return new ResponseEntity<>(locationService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<Long> delete(@NotNull @PathVariable Long id) {
        LOGGER.debug("delete({})", id);
        locationService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
