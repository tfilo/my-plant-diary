package sk.filo.plantdiary.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByOwnerUsername(String username);
}
