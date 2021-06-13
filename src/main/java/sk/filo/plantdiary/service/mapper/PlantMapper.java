package sk.filo.plantdiary.service.mapper;

import org.mapstruct.*;
import sk.filo.plantdiary.dao.domain.Plant;
import sk.filo.plantdiary.dao.domain.PlantType;
import sk.filo.plantdiary.service.so.CreatePlantSO;
import sk.filo.plantdiary.service.so.PlantBasicSO;
import sk.filo.plantdiary.service.so.PlantSO;
import sk.filo.plantdiary.service.so.PlantTypeSO;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PlantMapper {

    PlantSO toSO(Plant plant);

    @Mappings({
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "location", ignore = true)
    })
    void toBO(PlantSO plantSO, @MappingTarget Plant plant);

    @Mappings({
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "location", ignore = true)
    })
    Plant toBO(CreatePlantSO plant);

    PlantBasicSO toBasicSO(Plant plant);

    List<PlantTypeSO> toPlantTypeSOList(List<PlantType> plantTypes);

    PlantTypeSO toSO(PlantType plantType);

    PlantType toBO(PlantTypeSO plantTypeSO);

}
