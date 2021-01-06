package sk.filo.plantdiary.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByPlantId(Long plantId, Pageable page);
}
