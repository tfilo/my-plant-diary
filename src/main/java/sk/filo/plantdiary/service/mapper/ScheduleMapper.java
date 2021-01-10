package sk.filo.plantdiary.service.mapper;

import org.mapstruct.*;
import sk.filo.plantdiary.dao.domain.Schedule;
import sk.filo.plantdiary.service.so.CreateScheduleSO;
import sk.filo.plantdiary.service.so.ScheduleSO;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {EventTypeMapper.class})
public interface ScheduleMapper {

    ScheduleSO toSO(Schedule schedule);

    Schedule toBO(ScheduleSO schedule);

    @Mappings({
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "plant", ignore = true)
    })
    Schedule toBO(CreateScheduleSO schedule);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "plant", ignore = true)
    })
    void toBO(ScheduleSO scheduleSO, @MappingTarget Schedule event);

    List<ScheduleSO> toScheduleSOList(List<Schedule> events);

}
