package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.filo.plantdiary.service.UserService;
import sk.filo.plantdiary.service.so.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "user", description = "User endpoint")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @NotNull @RequestBody CreateUserSO createUserSO) {
        LOGGER.debug("create({})", createUserSO);
        userService.register(createUserSO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/activate")
    public ResponseEntity<Void> activateUser(@Valid @NotNull @RequestBody ActivateUserSO activateUserSO) {
        LOGGER.debug("activate({})", activateUserSO);
        userService.activate(activateUserSO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserSO> updateOwnUser(@Valid @NotNull @RequestBody UpdateUserSO updateUserSO) {
        LOGGER.debug("updateOwnUser({})", updateUserSO);
        return new ResponseEntity<>(userService.updateOwnUser(updateUserSO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserSO> getOwnUser() {
        LOGGER.debug("getOwnUser()");
        return new ResponseEntity<>(userService.getOwnUser(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteUser(@Valid @NotNull @RequestBody AuthSO authSO) {
        LOGGER.debug("delete()");
        if (userService.canDelete(authSO)) {
            userService.deleteUserAsync(authSO.getUsername());
        }
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

}
