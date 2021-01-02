package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.plantdiary.dao.domain.Location;
import sk.filo.plantdiary.dao.domain.User;
import sk.filo.plantdiary.dao.repository.LocationRepository;
import sk.filo.plantdiary.dao.repository.UserRepository;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.helper.AuthHelper;
import sk.filo.plantdiary.service.mapper.LocationMapper;
import sk.filo.plantdiary.service.so.CreateLocationSO;
import sk.filo.plantdiary.service.so.LocationSO;

import java.util.List;

@Service
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    private LocationRepository locationRepository;

    private UserRepository userRepository;

    private LocationMapper locationMapper;

    @Autowired
    public void setLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setLocationMapper(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    public LocationSO create(CreateLocationSO createLocationSO) {
        LOGGER.debug("create {}", createLocationSO);

        User owner = userRepository.findByUsername(AuthHelper.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name()));

        Location location = locationMapper.toBO(createLocationSO);
        location.setOwner(owner);

        return locationMapper.toSO(locationRepository.save(location));
    }

    public LocationSO update(LocationSO locationSO) {
        LOGGER.debug("update {}", locationSO);

        Location location = locationRepository.findById(locationSO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name()));

        if (location.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            locationMapper.toBO(locationSO, location);

            LOGGER.debug("update {}", location);
            return locationMapper.toSO(locationRepository.save(location));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name());
        }
    }

    public LocationSO getOne(Long id) {
        LOGGER.debug("getOne {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name()));

        if (location.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            return locationMapper.toSO(location);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name());
        }

    }

    public List<LocationSO> getAll() {
        LOGGER.debug("getAll");
        return locationMapper.toSOList(locationRepository.findByOwnerUsername(AuthHelper.getUsername()));
    }

    public void delete(Long id) {
        LOGGER.debug("delete {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name()));

        if (location.getOwner().getUsername().equals(AuthHelper.getUsername())) {
            locationRepository.delete(location);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.LOCATION_NOT_FOUND.name());
        }
    }
}
