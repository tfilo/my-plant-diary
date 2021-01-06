package sk.filo.plantdiary.service.mapper;

import org.mapstruct.*;
import sk.filo.plantdiary.dao.domain.Location;
import sk.filo.plantdiary.service.so.CreateLocationSO;
import sk.filo.plantdiary.service.so.LocationSO;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LocationMapper {

    LocationSO toSO(Location location);

    Location toBO(LocationSO location);

    Location toBO(CreateLocationSO createLocationSO);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "owner", ignore = true)
    })
    void toBO(LocationSO locationSO, @MappingTarget Location location);

    List<LocationSO> toSOList(List<Location> location);
}
