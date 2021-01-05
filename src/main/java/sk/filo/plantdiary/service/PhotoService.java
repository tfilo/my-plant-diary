package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.Photo;
import sk.filo.plantdiary.dao.domain.Plant;
import sk.filo.plantdiary.dao.repository.PhotoRepository;
import sk.filo.plantdiary.dao.repository.PlantRepository;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.helper.AuthHelper;
import sk.filo.plantdiary.service.mapper.LocationMapper;
import sk.filo.plantdiary.service.mapper.PhotoMapper;
import sk.filo.plantdiary.service.so.EventSO;
import sk.filo.plantdiary.service.so.PhotoSO;
import sk.filo.plantdiary.service.so.PhotoThumbnailSO;
import sk.filo.plantdiary.service.so.UpdatePhotoSO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhotoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoService.class);

    private PhotoRepository photoRepository;

    private PlantRepository plantRepository;

    private PhotoMapper photoMapper;

    @Autowired
    public void setPhotoRepository(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Autowired
    public void setPlantRepository(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Autowired
    public void setPhotoMapper(PhotoMapper photoMapper) {
        this.photoMapper = photoMapper;
    }

    public PhotoThumbnailSO create(PhotoSO photoSO) {
        LOGGER.debug("create {}", photoSO);

        Plant plant = plantRepository.findById(photoSO.getPlant().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

        if (plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            Photo photo = photoMapper.toBO(photoSO);
            photo.setPlant(plant);
            photo.setUploaded(LocalDateTime.now());

            try {
                photo.setThumbnail(resize(photo.getData(), 300f));
                photo.setData(resize(photo.getData(), 1920f));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionCode.PHOTO_PROCESSING_FAILED.name());
            }

            return photoMapper.toThumbnailSO(photoRepository.save(photo));
        } else {
            // when plant owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }
    }

    public PhotoThumbnailSO update(UpdatePhotoSO updatePhotoSO) {
        LOGGER.debug("update {}", updatePhotoSO);

        Photo photo = photoRepository.findById(updatePhotoSO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PHOTO_NOT_FOUND.name()));

        String username = AuthHelper.getUsername();

        LOGGER.debug(username);
        LOGGER.debug(photo.toString());

        if (photo.getPlant().getOwner().getUsername().equals(username)) {
            photoMapper.toBO(updatePhotoSO, photo);

            Plant plant = plantRepository.findById(updatePhotoSO.getPlant().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

            if (plant.getOwner().getUsername().equals(username)) {
                photo.setPlant(plant);
            } else { // if username doesn't match, throw exception that plant for this user was not found
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
            }

            return photoMapper.toThumbnailSO(photoRepository.save(photo));
        } else {
            // when plant owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }
    }

    public PhotoSO getOne(Long id) {
        LOGGER.debug("getOne {}", id);
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PHOTO_NOT_FOUND.name()));

        if (photo.getPlant().getOwner().getUsername().equals(AuthHelper.getUsername())) {
            return photoMapper.toSO(photo);
        } else {
            // when photo owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PHOTO_NOT_FOUND.name());
        }
    }

    @Transactional
    public Page<PhotoThumbnailSO> getAllByPlantIdPaginated(Long plantId, Integer page, Integer pageSize) {
        LOGGER.debug("getAllByPlantIdPaginated {} {} {}", plantId, page, pageSize);

        PageRequest pr = PageRequest.of(page, pageSize, Sort.by("uploaded").descending());

        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

        // when photo owned by different user, throw not found exception
        if (!plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }

        Page<Photo> photos = photoRepository.findByPlantId(plantId, pr);
        List<PhotoThumbnailSO> soList = photoMapper.toThumbnailSOList(photos.getContent());
        Page<PhotoThumbnailSO> results = new PageImpl<>(soList, photos.getPageable(), photos.getTotalElements());

        return results;
    }

    public void delete(Long id) {
        LOGGER.debug("delete {}", id);

        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PHOTO_NOT_FOUND.name()));

        if (photo.getPlant().getOwner().getUsername().equals(AuthHelper.getUsername())) {
            photoRepository.delete(photo);
        } else {
            // when photo owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PHOTO_NOT_FOUND.name());
        }
    }

    private byte[] resize(byte[] picture, float maxSizeLength) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(picture);
        BufferedImage bImage = ImageIO.read(bis);

        int height = bImage.getHeight();
        int width = bImage.getWidth();

        Float newHeight;
        Float newWidth;

        if (height < width) {
            newHeight = maxSizeLength;
            newWidth = width / (height / maxSizeLength);
        } else {
            newWidth = maxSizeLength;
            newHeight = height / (width / maxSizeLength);
        }

        Image scaled = bImage.getScaledInstance(newWidth.intValue(), newHeight.intValue(), Image.SCALE_SMOOTH);

        BufferedImage result = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = result.createGraphics();
        g2.drawImage(scaled, null, null);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(result, "jpg", bos);
        return bos.toByteArray();
    }
}
