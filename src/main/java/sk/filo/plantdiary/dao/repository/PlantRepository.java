package sk.filo.plantdiary.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.Plant;
import sk.filo.plantdiary.dao.domain.User;

import java.util.List;
import java.util.Optional;

public interface PlantRepository extends JpaRepository<Plant, Long> {

}
