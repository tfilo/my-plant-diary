package sk.filo.plantdiary.service.mapper;

import org.mapstruct.*;
import sk.filo.plantdiary.dao.domain.Photo;
import sk.filo.plantdiary.service.so.PhotoSO;
import sk.filo.plantdiary.service.so.PhotoThumbnailSO;
import sk.filo.plantdiary.service.so.UpdatePhotoSO;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {PlantMapper.class})
public interface PhotoMapper {

    @Mappings({
            @Mapping(target = "plant", ignore = true)
    })
    Photo toBO(PhotoSO photoSO);

    @Mappings({
            @Mapping(target = "plant", ignore = true)
    })
    void toBO(UpdatePhotoSO updatePhotoSO, @MappingTarget Photo photo);

    PhotoSO toSO(Photo photo);

    PhotoThumbnailSO toThumbnailSO(Photo photo);

    List<PhotoThumbnailSO> toThumbnailSOList(List<Photo> photo);
}
