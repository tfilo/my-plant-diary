package sk.filo.plantdiary.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.filo.plantdiary.service.UserService;
import sk.filo.plantdiary.service.so.CreateUserSO;
import sk.filo.plantdiary.service.so.UpdateUserSO;
import sk.filo.plantdiary.service.so.UserSO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "user", description = "User endpoint")
@RestController
@RequestMapping
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<Void> register(@Valid @NotNull @RequestBody CreateUserSO createUserSO) {
        LOGGER.debug("create({})", createUserSO);
        userService.register(createUserSO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/user/activate/{token}")
    public ResponseEntity<Void> activate(@Valid @NotNull @PathVariable String token) {
        LOGGER.debug("activate()");
        userService.activate(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<UserSO> updateOwnUser(@Valid @NotNull @RequestBody UpdateUserSO updateUserSO) {
        LOGGER.debug("updateOwnUser({})", updateUserSO);
        return new ResponseEntity<>(userService.updateOwnUser(updateUserSO), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserSO> getOwnUser() {
        LOGGER.debug("getOwnUser()");
        return new ResponseEntity<>(userService.getOwnUser(), HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Long> delete() {
        LOGGER.debug("delete()");
        userService.deleteOwnUser();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
