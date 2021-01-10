package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.plantdiary.dao.domain.User;
import sk.filo.plantdiary.dao.domain.UserActivation;
import sk.filo.plantdiary.dao.repository.EventRepository;
import sk.filo.plantdiary.dao.repository.LocationRepository;
import sk.filo.plantdiary.dao.repository.PlantRepository;
import sk.filo.plantdiary.dao.repository.UserRepository;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.jwt.JwtToken;
import sk.filo.plantdiary.service.helper.AuthHelper;
import sk.filo.plantdiary.service.mapper.UserMapper;
import sk.filo.plantdiary.service.so.*;

import java.util.UUID;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    private EventRepository eventRepository;

    private PlantRepository plantRepository;

    private LocationRepository locationRepository;

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    private JwtToken jwtToken;

    private MailService mailService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Autowired
    public void setPlantRepository(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Autowired
    public void setLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setJwtToken(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    // register new user, set enabled to false
    public void register(CreateUserSO createUserSO) {
        LOGGER.debug("register({})", createUserSO);
        if (userRepository.existsById(createUserSO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.USERNAME_IN_USE.name());
        }
        if (userRepository.existsByEmail(createUserSO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.EMAIL_IN_USE.name());
        }

        String activationToken = UUID.randomUUID().toString();
        mailService.sendVerificationEmail(createUserSO.getEmail(), activationToken);
        UserActivation ua = new UserActivation(createUserSO.getEmail(), activationToken);

        User user = userMapper.toBO(createUserSO);
        user.setUserActivation(ua);
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(createUserSO.getPassword()));
        userRepository.save(user);
    }

    // activate new user by link from email, set enabled to true
    public void activate(ActivateUserSO activateUserSO) {
        LOGGER.debug("activate({})", activateUserSO);
        User user = userRepository.findByUsernameAndUserActivationToken(activateUserSO.getUsername(), activateUserSO.getToken())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name()));

        user.setEnabled(true);
        user.setEmail(user.getUserActivation().getEmail()); // this is important when user only update email of existing account
        user.setUserActivation(null); // remove activation object
        userRepository.save(user);
    }

    // allow update only logged user
    public UserSO updateOwnUser(UpdateUserSO userSO) {
        LOGGER.debug("activate({})", userSO);
        User user = userRepository.findByUsername(AuthHelper.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name()));

        if (!user.getEmail().equals(userSO.getEmail())) {
            if (!passwordEncoder.matches(userSO.getOldPassword(), user.getPassword())) { // allow update email only if password provided
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_CREDENTIALS.name());
            }
            if (userRepository.existsByEmail(userSO.getEmail())) { // stop if new email is used by another account
                throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.EMAIL_IN_USE.name());
            }

            String activationToken = UUID.randomUUID().toString();
            mailService.sendVerificationEmail(userSO.getEmail(), activationToken);
            UserActivation ua = new UserActivation(userSO.getEmail(), activationToken);
            user.setUserActivation(ua);
        }

        userMapper.toBO(userSO, user);

        if (userSO.getPassword() != null && !userSO.getPassword().isBlank()) {
            if (!passwordEncoder.matches(userSO.getOldPassword(), user.getPassword())) { // allow update password only if old password provided
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_CREDENTIALS.name());
            }
            user.setPassword(passwordEncoder.encode(userSO.getPassword()));
        }
        LOGGER.debug("activate({})", user);
        return userMapper.toSO(userRepository.save(user));
    }

    // allow get only logged user
    public UserSO getOwnUser() {
        return userMapper.toSO(userRepository.findByUsername(AuthHelper.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name())));
    }

    public Boolean canDelete(AuthSO authSO) {
        if (!authSO.getUsername().equals(AuthHelper.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_CREDENTIALS.name());
        }
        User user = userRepository.findByUsername(authSO.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name()));
        if (!passwordEncoder.matches(authSO.getPassword(), user.getPassword())) { // allow delete only with valid password
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_CREDENTIALS.name());
        }
        return true;
    }

    // Async to avoid log wait when cascade delete running for lot of rows
    @Async("pdTaskExecutor")
    public void deleteUserAsync(String username) {
        User user = userRepository.getOne(username);
        userRepository.delete(user);
    }

}
