package sk.filo.plantdiary.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByPlantId(Long plantId, Pageable page);

    Page<Schedule> findByOwnerUsername(String username, Pageable page);
}
