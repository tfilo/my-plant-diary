package sk.filo.plantdiary.controller;

import org.apache.tomcat.jni.Time;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.dao.domain.*;
import sk.filo.plantdiary.dao.repository.*;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.UserService;
import sk.filo.plantdiary.service.so.*;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends BaseIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    PlantTypeRepository plantTypeRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventTypeRepository eventTypeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void userTest() throws Exception {
        super.setAuthentication(null); // test non authenticated user
        // create user
        // existing username
        CreateUserSO createUserSO = easyRandom.nextObject(CreateUserSO.class);
        createUserSO.setUsername("username");
        createUserSO.setEmail("mail@mail.sk");

        mvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createUserSO)))
                .andExpect(status().isConflict())
                .andExpect(status().reason(ExceptionCode.USERNAME_IN_USE.name()));

        // existing email
        createUserSO = easyRandom.nextObject(CreateUserSO.class);
        createUserSO.setEmail("user@user.sk");

        mvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createUserSO)))
                .andExpect(status().isConflict())
                .andExpect(status().reason(ExceptionCode.EMAIL_IN_USE.name()));

        createUserSO = easyRandom.nextObject(CreateUserSO.class);
        createUserSO.setEmail("mail@mail.sk");

        mvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createUserSO)))
                .andExpect(status().isCreated());

        Optional<User> user = userRepository.findByUsername(createUserSO.getUsername());
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getEnabled()).isFalse();
        assertThat(user.get()).usingRecursiveComparison().ignoringFields("enabled", "userActivation", "password", "plants", "locations").isEqualTo(createUserSO);

        // TODO verify if email was send to dev mail server
        // TODO read reived email in dev mail server and use activation token from there

        // Activate user
        // with wrong token
        ActivateUserSO activateUserSO = new ActivateUserSO();
        activateUserSO.setUsername(createUserSO.getUsername());
        activateUserSO.setToken("randomtoken");
        mvc.perform(MockMvcRequestBuilders.put("/api/user/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(activateUserSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.USER_NOT_FOUND.name()));

        // with wrong username
        activateUserSO.setUsername("username"); // this username belongs to another user
        activateUserSO.setToken(user.get().getUserActivation().getToken());
        mvc.perform(MockMvcRequestBuilders.put("/api/user/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(activateUserSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.USER_NOT_FOUND.name()));

        // with correct username and token
        activateUserSO = new ActivateUserSO();
        activateUserSO.setUsername(createUserSO.getUsername());
        activateUserSO.setToken(user.get().getUserActivation().getToken());
        mvc.perform(MockMvcRequestBuilders.put("/api/user/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(activateUserSO)))
                .andExpect(status().isOk());

        Optional<User> userEnabled = userRepository.findByUsername(createUserSO.getUsername());
        assertThat(userEnabled.isPresent()).isTrue();
        assertThat(userEnabled.get().getEnabled()).isTrue();
        assertThat(userEnabled.get().getUserActivation()).isNull();
        assertThat(userEnabled.get()).usingRecursiveComparison().ignoringFields("enabled", "userActivation").isEqualTo(user.get());

        super.setAuthentication(createUserSO.getUsername());
        // update user
        UpdateUserSO updateUserSO = easyRandom.nextObject(UpdateUserSO.class);
        updateUserSO.setEmail(createUserSO.getEmail());
        updateUserSO.setOldPassword(createUserSO.getPassword());

        String userpassword = updateUserSO.getPassword();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(updateUserSO)))
                .andExpect(status().isOk())
                .andReturn();

        UserSO userSO = mapFromJson(mvcResult.getResponse().getContentAsString(), UserSO.class);

        assertThat(userSO.getUsername()).isEqualTo(createUserSO.getUsername());
        assertThat(userSO.getPassword()).isNull(); // password can't be send to user, even in encoded form
        assertThat(userSO).usingRecursiveComparison().ignoringFields("oldPassword", "password", "username").isEqualTo(updateUserSO);

        // update user with wrong old password
        updateUserSO.setEmail(createUserSO.getEmail());
        updateUserSO.setOldPassword("wrongpassword");
        updateUserSO.setPassword("newpassword");
        mvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(updateUserSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.INVALID_CREDENTIALS.name()));

        // update email with already used email and wrong password
        updateUserSO.setEmail("user@user.sk"); // existing emal
        updateUserSO.setOldPassword("somewrongpassword");
        updateUserSO.setPassword(null); // don't update password anymore
        mvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(updateUserSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.INVALID_CREDENTIALS.name()));

        // update email with already used email
        updateUserSO.setEmail("user@user.sk"); // existing emal
        updateUserSO.setOldPassword(userpassword);
        mvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(updateUserSO)))
                .andExpect(status().isConflict())
                .andExpect(status().reason(ExceptionCode.EMAIL_IN_USE.name()));

        // update email
        updateUserSO.setEmail("usernew@user.sk");
        updateUserSO.setOldPassword(userpassword);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(updateUserSO)))
                .andExpect(status().isOk())
                .andReturn();

        userSO = mapFromJson(mvcResult.getResponse().getContentAsString(), UserSO.class);

        assertThat(userSO.getPassword()).isNull(); // password can't be send to user, even in encoded form
        assertThat(userSO.getEmail()).isNotEqualTo(updateUserSO.getEmail()); // email should not be updated until verified
        assertThat(userSO.getEmail()).isEqualTo(createUserSO.getEmail()); // email should be still unchanged
        assertThat(userSO).usingRecursiveComparison().ignoringFields("oldPassword", "password", "username", "email").isEqualTo(updateUserSO);

        Optional<User> userUpdated = userRepository.findByUsername(createUserSO.getUsername());
        assertThat(userUpdated.isPresent()).isTrue();
        assertThat(userUpdated.get().getEnabled()).isTrue();
        assertThat(userUpdated.get().getUserActivation()).isNotNull();
        assertThat(userUpdated.get().getUserActivation().getEmail()).isEqualTo(updateUserSO.getEmail()); // userActivation should have new email for verification

        // test get my user
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isOk())
                .andReturn();

        UserSO myUserSO = mapFromJson(mvcResult.getResponse().getContentAsString(), UserSO.class);
        assertThat(myUserSO).usingRecursiveComparison().isEqualTo(userSO); // should be equal to user returned from last update
    }

    @Test
    public void cascadeDeleteTest() throws Exception {
        int addLocations = 2;
        int addPlants = 3;
        int addPhotos = 2;
        int addEvents = 3;

        // store info about row count in database
        Long userBefore = userRepository.count();
        Long locationBefore = locationRepository.count();
        Long plantBefore = plantRepository.count();
        Long plantTypeBefore = plantTypeRepository.count();
        Long photoBefore = photoRepository.count();
        Long eventBefore = eventRepository.count();
        Long eventTypeBefore = eventTypeRepository.count();

        User user = easyRandom.nextObject(User.class);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode("BestPassword"));
        user.setUsername("testuser");
        user = userRepository.save(user);

        File photoFile = new File(getClass().getClassLoader().getResource("photo.jpg").getFile());
        byte[] testFile = Files.readAllBytes(photoFile.toPath());

        // create lot of data in database
        for (int i = 0; i < addLocations; i++) { // create 100 locations
            Location location = easyRandom.nextObject(Location.class);
            location.setOwner(user);
            location = locationRepository.save(location);
            for (int j = 0; j < addPlants; j++) { // create 100 plants in every location
                Plant plant = easyRandom.nextObject(Plant.class);
                plant.setType(plantTypeRepository.getOne(1L));
                plant.setOwner(user);
                plant.setLocation(location);
                plantRepository.save(plant);

                for (int k = 0; k < addPhotos; k++) {
                    Photo photo = easyRandom.nextObject(Photo.class);
                    photo.setData(testFile);
                    photo.setThumbnail(testFile);
                    photo.setPlant(plant);
                    photoRepository.save(photo);
                }

                for (int k = 0; k < addEvents; k++) { // insert 10000 events in every plant
                    Event event = easyRandom.nextObject(Event.class);
                    event.setPlant(plant);
                    event.setType(eventTypeRepository.getOne(1L));
                    eventRepository.save(event);
                    // TODO insert schedules
                }
            }
        }

        // check if expected number of rows stored
        Long userMid = userRepository.count();
        Long locationMid = locationRepository.count();
        Long plantMid = plantRepository.count();
        Long plantTypeMid = plantTypeRepository.count();
        Long photoMid = photoRepository.count();
        Long eventMid = eventRepository.count();
        Long eventTypeMid = eventTypeRepository.count();

        assertThat(userMid).isEqualTo(userBefore + 1);
        assertThat(locationMid).isEqualTo(locationBefore + addLocations);
        assertThat(plantMid).isEqualTo(plantBefore + addLocations * addPlants);
        assertThat(plantTypeMid).isEqualTo(plantTypeBefore);
        assertThat(photoMid).isEqualTo(photoBefore + addLocations * addPlants * addPhotos);
        assertThat(eventMid).isEqualTo(eventBefore + addLocations * addPlants * addEvents);
        assertThat(eventTypeMid).isEqualTo(eventTypeBefore);

        // cascade delete
        super.setAuthentication(user.getUsername()); // user need to be authenticated
        AuthSO authSO = new AuthSO();
        authSO.setUsername("testuser");
        authSO.setPassword("BestPassword");
        mvc.perform(MockMvcRequestBuilders.delete("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(authSO)))
                .andExpect(status().isAccepted());

        // check if all tables has original row count
        Long userAfter = userRepository.count();
        Long locationAfter = locationRepository.count();
        Long plantAfter = plantRepository.count();
        Long plantTypeAfter = plantTypeRepository.count();
        Long photoAfter = photoRepository.count();
        Long eventAfter = eventRepository.count();
        Long eventTypeAfter = eventTypeRepository.count();

        assertThat(userAfter).isEqualTo(userBefore);
        assertThat(locationAfter).isEqualTo(locationBefore);
        assertThat(plantAfter).isEqualTo(plantBefore);
        assertThat(plantTypeAfter).isEqualTo(plantTypeBefore);
        assertThat(photoAfter).isEqualTo(photoBefore);
        assertThat(eventAfter).isEqualTo(eventBefore);
        assertThat(eventTypeAfter).isEqualTo(eventTypeBefore);
    }
}
