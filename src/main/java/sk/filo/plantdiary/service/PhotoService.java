package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sk.filo.plantdiary.service.so.PhotoSO;
import sk.filo.plantdiary.service.so.PhotoThumbnailSO;

import java.util.List;

@Service
public class PhotoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoService.class);

    public PhotoThumbnailSO create(PhotoSO photoSO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PhotoThumbnailSO update(PhotoSO photoSO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PhotoSO getOne(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<PhotoThumbnailSO> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
