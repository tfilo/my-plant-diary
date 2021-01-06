package sk.filo.plantdiary.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.PlantType;

public interface PlantTypeRepository extends JpaRepository<PlantType, Long> {

}
