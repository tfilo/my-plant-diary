package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.filo.plantdiary.service.PlantService;
import sk.filo.plantdiary.service.so.CreatePlantSO;
import sk.filo.plantdiary.service.so.PlantSO;
import sk.filo.plantdiary.service.so.PlantTypeSO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @GetMapping("/plant")
    public ResponseEntity<List<PlantSO>> getAll() {
        LOGGER.debug("getAll()");
        return new ResponseEntity<>(plantService.getAll(), HttpStatus.OK);
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