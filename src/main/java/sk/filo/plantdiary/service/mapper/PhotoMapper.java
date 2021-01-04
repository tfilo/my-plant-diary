package sk.filo.plantdiary.service.mapper;

import org.mapstruct.*;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.Photo;
import sk.filo.plantdiary.dao.domain.Plant;
import sk.filo.plantdiary.service.so.*;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = { PlantMapper.class })
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

}
