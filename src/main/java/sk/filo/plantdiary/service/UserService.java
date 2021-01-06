package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.plantdiary.dao.domain.User;
import sk.filo.plantdiary.dao.repository.UserRepository;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.jwt.JwtToken;
import sk.filo.plantdiary.service.helper.AuthHelper;
import sk.filo.plantdiary.service.mapper.UserMapper;
import sk.filo.plantdiary.service.so.CreateUserSO;
import sk.filo.plantdiary.service.so.TokenSO;
import sk.filo.plantdiary.service.so.UpdateUserSO;
import sk.filo.plantdiary.service.so.UserSO;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    private JwtToken jwtToken;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    // register new user, set enabled to false
    public void register(CreateUserSO createUserSO) {
        if (userRepository.existsById(createUserSO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.USERNAME_IN_USE.name());
        }
        if (userRepository.existsByEmail(createUserSO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.EMAIL_IN_USE.name());
        }

        JwtToken.JwtUser jwtUser = new JwtToken.JwtUser();
        jwtUser.setUsername(createUserSO.getUsername());
        jwtUser.setEnabled(false);
        String activationToken = jwtToken.generateToken(jwtUser);
        // TODO send email with generated activationToken for activation
        new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "REGISTRATION NOT IMPLEMENTED YET");

        User user = userMapper.toBO(createUserSO);
        user.setEnabled(false);
        user.setPassword(passwordEncoder.encode(createUserSO.getPassword()));
        userRepository.save(user);
    }

    // activate new user by link from email, set enabled to true
    public void activate(String token) {
        // TODO check if invalid token will not pass this part
        JwtToken.JwtUser jwtUser = jwtToken.parseToken(token);

        if (jwtUser != null) {
            User user = userRepository.findByUsername(jwtUser.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name()));
            user.setEnabled(true);
            userRepository.save(user);
        }
        // TODO send email about successful activation
    }

    // allow update only logged user
    public UserSO updateOwnUser(UpdateUserSO userSO) {
        User user = userRepository.findByUsername(AuthHelper.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name()));

        if (user.getEmail().equals(userSO.getEmail())) {
            if (userRepository.existsByEmail(userSO.getEmail())) { // stop if new email is used by another account
                throw new ResponseStatusException(HttpStatus.CONFLICT, ExceptionCode.EMAIL_IN_USE.name());
            }
            new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "EMAIL_UPDATE_NOT_IMPLEMENTED_YET");
            // TODO send verification email
            // TODO create mechanism to store old email and return to old one if not activated in expected time period
        }

        userMapper.toBO(userSO, user);

        if (userSO.getPassword() != null && !userSO.getPassword().isBlank()) {
            if (!passwordEncoder.matches(userSO.getOldPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_CREDENTIALS.name());
            }
            user.setPassword(passwordEncoder.encode(userSO.getPassword()));
        }

        return userMapper.toSO(userRepository.save(user));
    }

    // allow get only logged user
    public UserSO getOwnUser() {
        return userMapper.toSO(userRepository.findByUsername(AuthHelper.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.USER_NOT_FOUND.name())));
    }

    // allow delete only own logged user
    public void deleteOwnUser() {
        // TODO find all data od user and delete all, maybe async task ?? there can be lot of data, try cascade delete
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "DELETE USER NOT IMPLEMENTED YET");
    }
}
