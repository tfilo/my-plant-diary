package sk.filo.plantdiary.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    Page<Plant> findByOwnerUsernameAndDeleted(String username, Boolean deleted, Pageable page);

    Page<Plant> findByOwnerUsernameAndDeletedAndLocationId(String username, Boolean deleted, Long locationId, Pageable page);

    Page<Plant> findByOwnerUsernameAndDeletedAndLocationIsNull(String username, Boolean deleted, Pageable page);
}
