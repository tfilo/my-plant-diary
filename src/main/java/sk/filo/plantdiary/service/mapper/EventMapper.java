package sk.filo.plantdiary.service.mapper;

import org.mapstruct.*;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.service.so.CreateEventSO;
import sk.filo.plantdiary.service.so.EventSO;
import sk.filo.plantdiary.service.so.UpdateEventSO;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {PlantMapper.class, EventTypeMapper.class})
public interface EventMapper {

    EventSO toSO(Event event);

    Event toBO(EventSO event);

    @Mappings({
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "plant", ignore = true)
    })
    Event toBO(CreateEventSO event);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "type", ignore = true)
    })
    void toBO(UpdateEventSO eventSO, @MappingTarget Event event);

    List<EventSO> toEventSOList(List<Event> events);
}
