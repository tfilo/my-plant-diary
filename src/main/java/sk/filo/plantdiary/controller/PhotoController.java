package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.filo.plantdiary.service.PhotoService;
import sk.filo.plantdiary.service.so.CreatePhotoSO;
import sk.filo.plantdiary.service.so.PhotoSO;
import sk.filo.plantdiary.service.so.PhotoThumbnailSO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@Tag(name = "photo", description = "Photo for plants endpoint")
@RestController
@RequestMapping
public class PhotoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/photo")
    public ResponseEntity<PhotoThumbnailSO> create(@NotNull @RequestParam("photo") MultipartFile photo, @Valid @NotNull @RequestBody CreatePhotoSO createPhotoSO) throws IOException {
        LOGGER.debug("create({}, {})", photo.getName(), createPhotoSO);
        PhotoSO photoSO = new PhotoSO(createPhotoSO.getDescription(), photo.getBytes());

        if (!StringUtils.hasText(createPhotoSO.getDescription())) {
            photoSO.setDescription(photo.getOriginalFilename());
        }

        LOGGER.debug("create() - photoSO: {}", photoSO);

        return new ResponseEntity<>(photoService.create(photoSO), HttpStatus.CREATED);
    }

    @PutMapping("/photo")
    public ResponseEntity<PhotoThumbnailSO> update(@Valid @NotNull @RequestBody PhotoSO photoSO) {
        LOGGER.debug("update({})", photoSO);
        return new ResponseEntity<>(photoService.update(photoSO), HttpStatus.OK);
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<PhotoSO> getOne(@NotNull @PathVariable Long id) {
        LOGGER.debug("getOne({})", id);
        return new ResponseEntity<>(photoService.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/photo/all/{plantId}")
    public ResponseEntity<List<PhotoThumbnailSO>> getAllByPlantId(@NotNull @PathVariable Long plantId) {
        LOGGER.debug("getAll()");
        return new ResponseEntity<>(photoService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/photo/{id}")
    public ResponseEntity<Long> delete(@NotNull @PathVariable Long id) {
        LOGGER.debug("delete({})", id);
        photoService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
