package sk.filo.plantdiary.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import sk.filo.plantdiary.dao.domain.EventType;
import sk.filo.plantdiary.service.so.EventTypeSO;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EventTypeMapper {

    List<EventTypeSO> toEventTypeSOList(List<EventType> eventTypes);

    EventTypeSO toSO(EventType eventType);

    EventType toBO(EventTypeSO eventTypeSO);

}
