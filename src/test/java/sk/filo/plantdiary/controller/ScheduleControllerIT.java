package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.so.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScheduleControllerIT extends BaseIntegrationTest {

    @Test
    public void scheduleTest() throws Exception {
        super.setAuthentication("username");

        // test create event on non existing plant and non existing event type
        CreateScheduleSO createScheduleSO = easyRandom.nextObject(CreateScheduleSO.class);
        createScheduleSO.setRepeatEvery(10);
        createScheduleSO.setNext(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MILLIS));
        createScheduleSO.getPlant().setId(100000L);
        createScheduleSO.getType().setId(100000L);

        mvc.perform(MockMvcRequestBuilders.post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createScheduleSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.EVENT_TYPE_NOT_FOUND.name()));

        // test create event on non existing plant but existing event type, minimal data
        createScheduleSO.getType().setId(1L);
        mvc.perform(MockMvcRequestBuilders.post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createScheduleSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        // test create event on existing plant and existing event, minimal data
        createScheduleSO.getPlant().setId(1L);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createScheduleSO)))
                .andExpect(status().isCreated())
                .andReturn();

        ScheduleSO scheduleSO = mapFromJson(mvcResult.getResponse().getContentAsString(), ScheduleSO.class);
        assertThat(scheduleSO).usingRecursiveComparison().ignoringFields("id", "type.code", "type.schedulable", "plant.name").isEqualTo(createScheduleSO);
        assertThat(scheduleSO.getId()).isNotNull();

        // try update saved event
        scheduleSO.setId(1L);
        scheduleSO.setNext(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MILLIS));
        scheduleSO.setRepeatEvery(5);
        scheduleSO.setAutoUpdate(true);
        scheduleSO.getPlant().setName("Name should not change");
        scheduleSO.getType().setCode("Code should not change");
        scheduleSO.getType().setSchedulable(!scheduleSO.getType().getSchedulable());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(scheduleSO)))
                .andExpect(status().isOk())
                .andReturn();

        ScheduleSO updatedScheduleSO = mapFromJson(mvcResult.getResponse().getContentAsString(), ScheduleSO.class);
        // check if correctly plant name and type name and schedulable wasn't modified
        assertThat(updatedScheduleSO).usingRecursiveComparison().isNotEqualTo(scheduleSO);
        // check if other fields was modified correctly
        assertThat(updatedScheduleSO).usingRecursiveComparison().ignoringFields("type.code", "type.schedulable", "plant.name").isEqualTo(scheduleSO);

        // test get of non existing record
        mvc.perform(MockMvcRequestBuilders.get("/api/schedule/100000"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.SCHEDULE_NOT_FOUND.name()));

        // test get of existing record
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/schedule/" + updatedScheduleSO.getId()))
                .andExpect(status().isOk())
                .andReturn();

        scheduleSO = mapFromJson(mvcResult.getResponse().getContentAsString(), ScheduleSO.class);
        assertThat(scheduleSO).usingRecursiveComparison().isEqualTo(updatedScheduleSO);

        // get All tests
        mvc.perform(MockMvcRequestBuilders.get("/api/schedule?plantId=100000&page=0&pageSize=10"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/schedule?plantId=1&page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        Page<ScheduleSO> schedules = mapPagedResponse(mvcResult.getResponse().getContentAsString(), ScheduleSO.class);
        assertThat(schedules.getTotalElements()).isEqualTo(1);

        // check delete
        mvc.perform(MockMvcRequestBuilders.delete("/api/schedule/" + scheduleSO.getId()))
                .andExpect(status().isOk());

        // check if deleted
        mvc.perform(MockMvcRequestBuilders.get("/api/schedule/" + scheduleSO.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.SCHEDULE_NOT_FOUND.name()));
    }
}
