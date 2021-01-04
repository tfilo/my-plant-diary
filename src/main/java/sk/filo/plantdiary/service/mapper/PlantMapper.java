package sk.filo.plantdiary.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.Plant;
import sk.filo.plantdiary.service.so.*;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PlantMapper {

    PlantSO plantToPlantSO(Plant plant);

    Plant plantSOToPlant(PlantSO plant);

    Plant createPlantSOToPlant(CreatePlantSO plant);

    PlantBasicSO plantToPlantBasicSO(Plant plant);

}
