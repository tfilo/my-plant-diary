package sk.filo.plantdiary.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, Long> {

}
