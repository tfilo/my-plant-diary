package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.filo.plantdiary.service.PhotoService;
import sk.filo.plantdiary.service.so.CreatePhotoSO;
import sk.filo.plantdiary.service.so.PhotoSO;
import sk.filo.plantdiary.service.so.PhotoThumbnailSO;
import sk.filo.plantdiary.service.so.UpdatePhotoSO;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Tag(name = "photo", description = "Photo for plants endpoint")
@RestController
@RequestMapping("/api")
public class PhotoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoController.class);

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("/photo")
    public ResponseEntity<PhotoThumbnailSO> create(
            @RequestParam("photo") @NotNull MultipartFile photo,
            @RequestPart("createPhotoSO") @Valid @NotNull CreatePhotoSO createPhotoSO
    ) throws IOException {
        LOGGER.debug("create({}, {})", photo.getName(), createPhotoSO);
        PhotoSO photoSO = new PhotoSO(createPhotoSO, photo.getBytes());

        if (!StringUtils.hasText(createPhotoSO.getDescription())) {
            photoSO.setDescription(photo.getOriginalFilename());
        }

        LOGGER.debug("create() - photoSO: {}", photoSO);

        return new ResponseEntity<>(photoService.create(photoSO), HttpStatus.CREATED);
    }

    @PutMapping("/photo")
    public ResponseEntity<PhotoThumbnailSO> update(@Valid @NotNull @RequestBody UpdatePhotoSO updatePhotoSO) {
        LOGGER.debug("update({})", updatePhotoSO);
        return new ResponseEntity<>(photoService.update(updatePhotoSO), HttpStatus.OK);
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<PhotoSO> getOne(@NotNull @PathVariable Long id) {
        LOGGER.debug("getOne({})", id);
        return new ResponseEntity<>(photoService.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/photo/all/{plantId}")
    public ResponseEntity<Page<PhotoThumbnailSO>> getAllByPlantId(
            @NotNull @PathVariable Long plantId,
            @NotNull @Min(0) @RequestParam Integer page,
            @NotNull @Min(5) @Max(100) @RequestParam Integer pageSize) {
        LOGGER.debug("getAllByPlantId({},{},{})", plantId, page, pageSize);
        return new ResponseEntity<>(photoService.getAllByPlantIdPaginated(plantId, page, pageSize), HttpStatus.OK);
    }

    @DeleteMapping("/photo/{id}")
    public ResponseEntity<Long> delete(@NotNull @PathVariable Long id) {
        LOGGER.debug("delete({})", id);
        photoService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
