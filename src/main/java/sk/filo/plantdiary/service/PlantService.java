package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.plantdiary.dao.domain.Location;
import sk.filo.plantdiary.dao.domain.Plant;
import sk.filo.plantdiary.dao.domain.PlantType;
import sk.filo.plantdiary.dao.domain.User;
import sk.filo.plantdiary.dao.repository.LocationRepository;
import sk.filo.plantdiary.dao.repository.PlantRepository;
import sk.filo.plantdiary.dao.repository.PlantTypeRepository;
import sk.filo.plantdiary.dao.repository.UserRepository;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.helper.AuthHelper;
import sk.filo.plantdiary.service.mapper.PlantMapper;
import sk.filo.plantdiary.service.so.CreatePlantSO;
import sk.filo.plantdiary.service.so.PlantSO;
import sk.filo.plantdiary.service.so.PlantTypeSO;

import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantService.class);

    private PlantRepository plantRepository;

    private PlantTypeRepository plantTypeRepository;

    private UserRepository userRepository;

    private LocationRepository locationRepository;

    private PlantMapper plantMapper;

    @Autowired
    public void setPlantRepository(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Autowired
    public void setPlantTypeRepository(PlantTypeRepository plantTypeRepository) {
        this.plantTypeRepository = plantTypeRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Autowired
    public void setPlantMapper(PlantMapper plantMapper) {
        this.plantMapper = plantMapper;
    }

    public PlantSO create(CreatePlantSO createPlantSO) {
        LOGGER.debug("create {}", createPlantSO);

        User owner = userRepository.findByUsername(AuthHelper.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name()));

        Plant plant = plantMapper.toBO(createPlantSO);
        plant.setOwner(owner);
        plant.setDeleted(false);
        setPlantType(createPlantSO.getType().getId(), plant);
        if (createPlantSO.getLocation() != null) {
            setLocation(createPlantSO.getLocation().getId(), plant);
        }
        LOGGER.debug("create {}", plant);
        return plantMapper.toSO(plantRepository.save(plant));
    }

    public PlantSO update(PlantSO plantSO) {
        LOGGER.debug("update {}", plantSO);

        Plant plant = plantRepository.findById(plantSO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

        if (plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            plantMapper.toBO(plantSO, plant);
            setPlantType(plantSO.getType().getId(), plant);
            if (plantSO.getLocation() != null) {
                setLocation(plantSO.getLocation().getId(), plant);
            } else {
                plant.setLocation(null);
            }
            LOGGER.debug("update {}", plant);
            return plantMapper.toSO(plantRepository.save(plant));
        } else {
            // when plant owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }
    }

    public PlantSO getOne(Long id) {
        LOGGER.debug("getOne {}", id);
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

        if (plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            return plantMapper.toSO(plant);
        } else {
            // when plant owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }
    }

    public Page<PlantSO> getAllPaginated(Optional<Boolean> deleted, Integer page, Integer pageSize) {
        LOGGER.debug("getAllPaginated {} {} {}", deleted, page, pageSize);

        PageRequest pr = PageRequest.of(page, pageSize, Sort.by("name.").ascending());

        // don't need to check for location owner, because plant has owner
        Page<Plant> plants = plantRepository.findByOwnerUsernameAndDeleted(AuthHelper.getUsername(), deleted.orElse(false), pr);

        return plants.map(plantMapper::toSO);
    }

    public Page<PlantSO> getAllPaginatedByLocation(Optional<Long> locationId, Optional<Boolean> deleted, Integer page, Integer pageSize) {
        LOGGER.debug("getAllPaginatedByLocation {} {} {} {}", locationId, deleted, page, pageSize);

        PageRequest pr = PageRequest.of(page, pageSize, Sort.by("name.").ascending());

        // don't need to check for location owner, because plant has owner
        Page<Plant> plants;
        if (locationId.isPresent()) {
            plants = plantRepository.findByOwnerUsernameAndDeletedAndLocationId(
                    AuthHelper.getUsername(),
                    deleted.orElse(false),
                    locationId.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name())),
                    pr);
        } else {
            plants = plantRepository.findByOwnerUsernameAndDeletedAndLocationIsNull(AuthHelper.getUsername(), deleted.orElse(false), pr);
        }

        return plants.map(plantMapper::toSO);
    }

    public void delete(Long id) {
        LOGGER.debug("delete {}", id);

        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name()));

        if (plant.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            plant.setDeleted(true);
            plantRepository.save(plant);
        } else {
            // when plant owned by different user, throw not found exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_NOT_FOUND.name());
        }
    }

    public List<PlantTypeSO> getAllTypes() {
        LOGGER.debug("getAllTypes");
        return plantMapper.toPlantTypeSOList(plantTypeRepository.findAll());
    }

    private void setPlantType(Long plantTypeId, Plant plant) {
        LOGGER.debug("setPlantType {} {}", plantTypeId, plant);
        PlantType plantType = plantTypeRepository.findById(plantTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.PLANT_TYPE_NOT_FOUND.name()));

        LOGGER.debug("setPlantType {}", plantType);

        plant.setType(plantType);
    }

    private void setLocation(Long locationId, Plant plant) {
        LOGGER.debug("setLocation {} {}", locationId, plant);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name()));

        LOGGER.debug("setLocation {}", location);

        if (location.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            plant.setLocation(location);
        } else { // if username doesn't match, throw exception that location was not found
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name());
        }
    }
}
