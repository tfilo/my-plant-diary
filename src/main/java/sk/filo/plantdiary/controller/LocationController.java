package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/location")
public class LocationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationSO> createLocation(@Valid @NotNull @RequestBody CreateLocationSO createLocationSO) {
        LOGGER.debug("create({})", createLocationSO);
        return new ResponseEntity<>(locationService.create(createLocationSO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<LocationSO> updateLocation(@Valid @NotNull @RequestBody LocationSO locationSO) {
        LOGGER.debug("update({})", locationSO);
        return new ResponseEntity<>(locationService.update(locationSO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationSO> getOneLocation(@NotNull @PathVariable Long id) {
        LOGGER.debug("getOne({})", id);
        return new ResponseEntity<>(locationService.getOne(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LocationSO>> getAllLocations() {
        LOGGER.debug("getAll()");
        return new ResponseEntity<>(locationService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteLocation(@NotNull @PathVariable Long id) {
        LOGGER.debug("delete({})", id);
        locationService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
