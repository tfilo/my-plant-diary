package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sk.filo.plantdiary.service.so.CreatePlantSO;
import sk.filo.plantdiary.service.so.PlantSO;
import sk.filo.plantdiary.service.so.PlantTypeSO;

import java.util.List;

@Service
public class PlantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantService.class);

    public PlantSO create(CreatePlantSO createPlantSO) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public PlantSO update(PlantSO plantSO) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public PlantSO getOne(Long id) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public List<PlantSO> getAll() {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public void delete(Long id) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    public List<PlantTypeSO> getAllTypes() {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }
}
