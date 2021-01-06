package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.so.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlantControllerIT extends BaseIntegrationTest {

    @Test
    public void plantTest() throws Exception {
        super.setAuthentication("user");

        // create plant
        CreatePlantSO createPlantSO = easyRandom.nextObject(CreatePlantSO.class);
        createPlantSO.getLocation().setId(100000L);
        createPlantSO.getType().setId(100000L);

        mvc.perform(MockMvcRequestBuilders.post("/api/plant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createPlantSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_TYPE_NOT_FOUND.name()));

        createPlantSO.getType().setId(1L);

        mvc.perform(MockMvcRequestBuilders.post("/api/plant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createPlantSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.LOCATION_NOT_FOUND.name()));

        createPlantSO.getLocation().setId(1L);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/plant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createPlantSO)))
                .andExpect(status().isCreated())
                .andReturn();

        PlantSO plantSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plantSO).usingRecursiveComparison().ignoringFields("type.code", "location.name", "id", "deleted").isEqualTo(createPlantSO);
        assertThat(plantSO.getId()).isNotNull();
        assertThat(plantSO.getDeleted()).isFalse();

        // Update plant
        plantSO.setName("New Name");
        plantSO.setDescription("New Description");

        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/plant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(plantSO)))
                .andExpect(status().isOk())
                .andReturn();

        PlantSO updatedPlantSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(updatedPlantSO).usingRecursiveComparison().ignoringFields().isEqualTo(plantSO);

        // Update plant, remove location
        plantSO.setLocation(null);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/plant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(plantSO)))
                .andExpect(status().isOk())
                .andReturn();

        updatedPlantSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(updatedPlantSO).usingRecursiveComparison().ignoringFields().isEqualTo(plantSO);

        // get one plant
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant/1"))
                .andExpect(status().isOk())
                .andReturn();

        plantSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plantSO.getName()).isEqualTo("Test plant 1");
        assertThat(plantSO.getDescription()).isNull();
        assertThat(plantSO.getDeleted()).isEqualTo(false);
        assertThat(plantSO.getId()).isEqualTo(1L);
        assertThat(plantSO.getLocation()).isNull();
        assertThat(plantSO.getType()).isNull();

        // get one plant of different owner
        mvc.perform(MockMvcRequestBuilders.get("/api/plant/2"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        // get all non deleted
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant?page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        Page<PlantSO> plants = mapPagedResponse(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plants.getTotalElements()).isEqualTo(2);

        // get all deleted
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant?deleted=true&page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        plants = mapPagedResponse(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plants.getTotalElements()).isEqualTo(0);

        super.setAuthentication("user3");

        // get all by location but without location
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant/byLocation?page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        plants = mapPagedResponse(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plants.getTotalElements()).isEqualTo(1);

        // get all by location but without location and deleted
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant/byLocation?deleted=true&page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        plants = mapPagedResponse(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plants.getTotalElements()).isEqualTo(2);

        // get all by location 1
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant/byLocation?locationId=1&page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        plants = mapPagedResponse(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plants.getTotalElements()).isEqualTo(2);

        // get all by location 1 and deleted
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant/byLocation?locationId=1&deleted=true&page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        plants = mapPagedResponse(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plants.getTotalElements()).isEqualTo(1);

        // check if not deleted
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant/2"))
                .andExpect(status().isOk())
                .andReturn();

        plantSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plantSO.getDeleted()).isFalse();

        // try delete
        mvc.perform(MockMvcRequestBuilders.delete("/api/plant/2"))
                .andExpect(status().isOk());

        // check if deleted
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant/2"))
                .andExpect(status().isOk())
                .andReturn();

        plantSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PlantSO.class);
        assertThat(plantSO.getDeleted()).isTrue();

        // get all plantTypes
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/plant/type"))
                .andExpect(status().isOk())
                .andReturn();
        List<PlantTypeSO> eventTypes = mapListFromJson(mvcResult.getResponse().getContentAsString(), PlantTypeSO.class);
        assertThat(eventTypes.size()).isEqualTo(11);

        // just random check returned data
        assertThat(eventTypes.get(0).getCode()).isEqualTo("ORNAMENTAL");
        assertThat(eventTypes.get(3).getCode()).isEqualTo("SUCCULENT");
        assertThat(eventTypes.get(7).getCode()).isEqualTo("BUSH");
    }
}
