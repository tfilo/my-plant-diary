package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.so.LocationSO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LocationControllerIT extends BaseIntegrationTest {

    @Test
    public void locationTest() throws Exception {
        super.setAuthentication("user");

        // test create location
        LocationSO locationSO = easyRandom.nextObject(LocationSO.class);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(locationSO)))
                .andExpect(status().isCreated())
                .andReturn();

        LocationSO createdLocationSO = mapFromJson(mvcResult.getResponse().getContentAsString(), LocationSO.class);

        assertThat(createdLocationSO).usingRecursiveComparison().ignoringFields("id").isEqualTo(locationSO);
        assertThat(createdLocationSO.getId()).isNotNull();

        // test update location name
        createdLocationSO.setName("Updated location name");
        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createdLocationSO)))
                .andExpect(status().isOk())
                .andReturn();

        LocationSO updatedLocationSO = mapFromJson(mvcResult.getResponse().getContentAsString(), LocationSO.class);
        assertThat(updatedLocationSO).usingRecursiveComparison().isEqualTo(createdLocationSO);

        // get existing location
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/location/" + updatedLocationSO.getId()))
                .andExpect(status().isOk())
                .andReturn();
        locationSO = mapFromJson(mvcResult.getResponse().getContentAsString(), LocationSO.class);
        assertThat(locationSO).usingRecursiveComparison().isEqualTo(updatedLocationSO);

        // get non existing location
        mvc.perform(MockMvcRequestBuilders.get("/api/location/100000"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.LOCATION_NOT_FOUND.name()));

        // add 10 new locations
        for (int i = 0; i < 10; i++) {
            locationSO.setName("Location " + (i + 2));
            mvc.perform(MockMvcRequestBuilders.post("/api/location")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(locationSO)))
                    .andExpect(status().isCreated());
        }

        // get all locations
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/location"))
                .andExpect(status().isOk())
                .andReturn();
        List<LocationSO> locations = mapListFromJson(mvcResult.getResponse().getContentAsString(), LocationSO.class);

        assertThat(locations.size()).isEqualTo(12);

        // delete location
        mvc.perform(MockMvcRequestBuilders.delete("/api/location/" + locationSO.getId()))
                .andExpect(status().isOk());

        // check if deleted
        mvc.perform(MockMvcRequestBuilders.get("/api/location/" + locationSO.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.LOCATION_NOT_FOUND.name()));

        // get location for next tests
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/location/1"))
                .andExpect(status().isOk())
                .andReturn();
        LocationSO anotherToDelete = mapFromJson(mvcResult.getResponse().getContentAsString(), LocationSO.class);

        // get all locations under different user
        super.setAuthentication("user3");
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/location"))
                .andExpect(status().isOk())
                .andReturn();
        locations = mapListFromJson(mvcResult.getResponse().getContentAsString(), LocationSO.class);
        assertThat(locations.isEmpty()).isTrue();

        // try delete location of different user
        mvc.perform(MockMvcRequestBuilders.delete("/api/location/" + anotherToDelete.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.LOCATION_NOT_FOUND.name()));

        // try update location of different user
        mvc.perform(MockMvcRequestBuilders.put("/api/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(anotherToDelete)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.LOCATION_NOT_FOUND.name()));
    }
}
