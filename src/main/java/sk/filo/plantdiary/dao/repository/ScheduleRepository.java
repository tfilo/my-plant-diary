package sk.filo.plantdiary.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.Schedule;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByPlantId(Long plantId, Pageable page);

    Optional<Schedule> findFirstByPlantIdAndTypeId(Long plantId, Long typeId);

    Boolean existsByPlantIdAndTypeId(Long plantId, Long typeId);

    Page<Schedule> findByOwnerUsername(String username, Pageable page);
}
