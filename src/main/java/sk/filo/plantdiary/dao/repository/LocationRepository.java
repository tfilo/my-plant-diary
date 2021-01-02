package sk.filo.plantdiary.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.Location;
import sk.filo.plantdiary.dao.domain.Plant;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByOwnerUsername(String username);
}
