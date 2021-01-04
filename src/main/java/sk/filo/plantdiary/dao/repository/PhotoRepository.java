package sk.filo.plantdiary.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.filo.plantdiary.dao.domain.Event;
import sk.filo.plantdiary.dao.domain.Photo;
import sk.filo.plantdiary.dao.domain.Plant;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Page<Photo> findByPlantId(Long plantId, Pageable page);
}
