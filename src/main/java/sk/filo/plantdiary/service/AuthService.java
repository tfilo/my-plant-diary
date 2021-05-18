package sk.filo.plantdiary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.filo.plantdiary.dao.domain.User;
import sk.filo.plantdiary.dao.repository.UserRepository;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.jwt.JwtToken;
import sk.filo.plantdiary.service.helper.AuthHelper;
import sk.filo.plantdiary.service.so.AuthSO;
import sk.filo.plantdiary.service.so.TokenSO;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private JwtToken jwtToken;

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @Autowired
    public void setJwtToken(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TokenSO authenticateUser(AuthSO authSO) {
        LOGGER.debug("authenticateUser({})", authSO);

        User user = userRepository.findByUsername(authSO.getUsername().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_CREDENTIALS.name()));

        if (!user.getEnabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionCode.DISABLED_USER.name());
        }
        if (!passwordEncoder.matches(authSO.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_CREDENTIALS.name());
        }

        TokenSO tokenSO = generateToken(user);
        LOGGER.debug("authenticateUser({}) - {}", authSO, tokenSO);
        return tokenSO;
    }

    public TokenSO renewToken() {
        LOGGER.debug("renewToken()");
        final String username = AuthHelper.getUsername();
        if (username!=null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ExceptionCode.INVALID_CREDENTIALS.name()));

            if (!user.getEnabled()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionCode.DISABLED_USER.name());
            }

            TokenSO tokenSO = generateToken(user);
            LOGGER.debug("renewToken() - {}", tokenSO);
            return tokenSO;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, ExceptionCode.SESSION_EXPIRED.name());
    }

    private TokenSO generateToken(User user) {
        JwtToken.JwtUser jwtUser = new JwtToken.JwtUser();
        jwtUser.setUsername(user.getUsername());
        jwtUser.setEnabled(user.getEnabled());

        TokenSO tokenSO = new TokenSO();
        tokenSO.setToken(jwtToken.generateToken(jwtUser));
        return tokenSO;
    }
}
