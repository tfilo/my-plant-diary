package sk.filo.plantdiary.service.mapper;

import org.mapstruct.*;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.EventType;
import sk.filo.plantdiary.service.so.CreateEventSO;
import sk.filo.plantdiary.service.so.EventSO;
import sk.filo.plantdiary.service.so.EventTypeSO;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = { PlantMapper.class })
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
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "plant", ignore = true)
    })
    void toBO(EventSO eventSO, @MappingTarget Event event);

    List<EventSO> toEventSOList(List<Event> events);

    List<EventTypeSO> toEventTypeSOList(List<EventType> eventTypes);

    EventTypeSO eventTypeToEventTypeSO(EventType eventType);

    EventType eventTypeSOToEventType(EventTypeSO eventTypeSO);

}
