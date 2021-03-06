package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.filo.plantdiary.service.AuthService;
import sk.filo.plantdiary.service.so.AuthSO;
import sk.filo.plantdiary.service.so.TokenSO;

import javax.validation.Valid;
import java.security.Principal;

@Tag(name = "authenticate", description = "User authentication endpoint")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<TokenSO> authenticateUser(@Valid @RequestBody AuthSO authSO) {
        LOGGER.debug("authenticateUser({})", authSO);
        return new ResponseEntity<>(authService.authenticateUser(authSO), HttpStatus.OK);
    }

    @PostMapping("/renew")
    public ResponseEntity<TokenSO> renewToken() {
        LOGGER.debug("renewToken()");
        return new ResponseEntity<>(authService.renewToken(), HttpStatus.OK);
    }
}
