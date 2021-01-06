package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.filo.plantdiary.service.PlantService;
import sk.filo.plantdiary.service.so.CreatePlantSO;
import sk.filo.plantdiary.service.so.PlantSO;
import sk.filo.plantdiary.service.so.PlantTypeSO;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Tag(name = "plant", description = "Plant endpoint")
@RestController
@RequestMapping
public class PlantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantController.class);

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @PostMapping("/plant")
    public ResponseEntity<PlantSO> create(@Valid @NotNull @RequestBody CreatePlantSO createPlantSO) {
        LOGGER.debug("create({})", createPlantSO);
        return new ResponseEntity<>(plantService.create(createPlantSO), HttpStatus.CREATED);
    }

    @PutMapping("/plant")
    public ResponseEntity<PlantSO> update(@Valid @NotNull @RequestBody PlantSO plantSO) {
        LOGGER.debug("update({})", plantSO);
        return new ResponseEntity<>(plantService.update(plantSO), HttpStatus.OK);
    }

    @GetMapping("/plant/{id}")
    public ResponseEntity<PlantSO> getOne(@NotNull @PathVariable Long id) {
        LOGGER.debug("getOne({})", id);
        return new ResponseEntity<>(plantService.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/plant/all")
    public ResponseEntity<Page<PlantSO>> getAll(
            @RequestParam Optional<Boolean> deleted,
            @NotNull @Min(0) @RequestParam Integer page,
            @NotNull @Min(5) @Max(100) @RequestParam Integer pageSize) {
        LOGGER.debug("getAll({}, {}, {})", deleted, page, pageSize);
        return new ResponseEntity<>(plantService.getAllPaginated(deleted, page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/plant/byLocation/all")
    public ResponseEntity<Page<PlantSO>> getAllByLocation(
            @RequestParam Optional<Long> locationId,
            @RequestParam Optional<Boolean> deleted,
            @NotNull @Min(0) @RequestParam Integer page,
            @NotNull @Min(5) @Max(100) @RequestParam Integer pageSize) {
        LOGGER.debug("getAllByLocation({}, {}, {})", deleted, page, pageSize);
        return new ResponseEntity<>(plantService.getAllPaginatedByLocation(locationId, deleted, page, pageSize), HttpStatus.OK);
    }

    @DeleteMapping("/plant/{id}")
    public ResponseEntity<Long> delete(@NotNull @PathVariable Long id) {
        LOGGER.debug("delete({})", id);
        plantService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/plant/type")
    public ResponseEntity<List<PlantTypeSO>> getAllTypes() {
        LOGGER.debug("getAllTypes()");
        return new ResponseEntity<>(plantService.getAllTypes(), HttpStatus.OK);
    }
}
