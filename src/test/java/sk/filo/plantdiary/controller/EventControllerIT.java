package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.so.CreateEventSO;
import sk.filo.plantdiary.service.so.EventSO;
import sk.filo.plantdiary.service.so.EventTypeSO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerIT extends BaseIntegrationTest {

    @Test
    public void eventTest() throws Exception {
        super.setAuthentication("user");

        // test create event on non existing plant and non existing event type
        CreateEventSO createEventSO = easyRandom.nextObject(CreateEventSO.class);
        createEventSO.getPlant().setId(100000L);
        createEventSO.getType().setId(100000L);

        mvc.perform(MockMvcRequestBuilders.post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createEventSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.EVENT_TYPE_NOT_FOUND.name()));

        // test create event on non existing plant but existing event type, minimal data
        createEventSO.getType().setId(1L);
        mvc.perform(MockMvcRequestBuilders.post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createEventSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        // test create event on existing plant and existing event, minimal data
        createEventSO.getPlant().setId(1L);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createEventSO)))
                .andExpect(status().isCreated())
                .andReturn();

        EventSO eventSO = mapFromJson(mvcResult.getResponse().getContentAsString(), EventSO.class);
        assertThat(eventSO).usingRecursiveComparison().ignoringFields("id", "type.code", "type.schedulable", "plant.name").isEqualTo(createEventSO);
        assertThat(eventSO.getId()).isNotNull();

        // try update saved event
        eventSO.setId(1L);
        eventSO.setDateTime(LocalDateTime.now());
        eventSO.setDescription("New event description");

        eventSO.getPlant().setName("Name should not change");
        eventSO.getType().setCode("Code should not change");
        eventSO.getType().setSchedulable(!eventSO.getType().getSchedulable());

        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(eventSO)))
                .andExpect(status().isOk())
                .andReturn();

        EventSO updatedEventSO = mapFromJson(mvcResult.getResponse().getContentAsString(), EventSO.class);
        // check if correctly plant name and type name and schedulable wasn't modified
        assertThat(updatedEventSO).usingRecursiveComparison().isNotEqualTo(eventSO);
        // check if other fields was modified correctly
        assertThat(updatedEventSO).usingRecursiveComparison().ignoringFields("type.code", "type.schedulable", "plant.name").isEqualTo(eventSO);

        // test get of non existing record
        mvc.perform(MockMvcRequestBuilders.get("/api/event/100000"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.EVENT_NOT_FOUND.name()));

        // test get of existing record
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/" + updatedEventSO.getId()))
                .andExpect(status().isOk())
                .andReturn();

        eventSO = mapFromJson(mvcResult.getResponse().getContentAsString(), EventSO.class);
        assertThat(eventSO).usingRecursiveComparison().isEqualTo(updatedEventSO);

        // get All tests
        mvc.perform(MockMvcRequestBuilders.get("/api/event/all/100000?page=0&pageSize=10"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        Page<EventSO> events = mapPagedResponse(mvcResult.getResponse().getContentAsString(), EventSO.class);
        assertThat(events.getTotalElements()).isEqualTo(1);

        // add 24 more events
        LocalDateTime now = LocalDateTime.now().minusDays(50);
        List<Integer> range = IntStream.range(1, 100).boxed().collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(range);

        for (int i = 0; i < 24; i++) {
            createEventSO.setDescription("Event " + (i + 2));
            createEventSO.setDateTime(now.plusDays(range.get(i)));
            mvc.perform(MockMvcRequestBuilders.post("/api/event")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapToJson(createEventSO))).andExpect(status().isCreated());
        }

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        events = mapPagedResponse(mvcResult.getResponse().getContentAsString(), EventSO.class);
        assertThat(events.getTotalElements()).isEqualTo(25);

        // check order of all events by date
        Iterator<EventSO> iterator = events.iterator();
        EventSO last = iterator.next();
        while (iterator.hasNext()) {
            EventSO actual = iterator.next();
            assertThat(last.getDateTime().isAfter(actual.getDateTime())).isTrue();
            last = actual;
        }

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=1&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        events = mapPagedResponse(mvcResult.getResponse().getContentAsString(), EventSO.class);

        iterator = events.iterator();
        while (iterator.hasNext()) { // check if ordered by date
            EventSO actual = iterator.next();
            assertThat(last.getDateTime().isAfter(actual.getDateTime())).isTrue();
            last = actual;
        }

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=2&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();
        events = mapPagedResponse(mvcResult.getResponse().getContentAsString(), EventSO.class);

        iterator = events.iterator();
        while (iterator.hasNext()) { // check if ordered by date
            EventSO actual = iterator.next();
            assertThat(last.getDateTime().isAfter(actual.getDateTime())).isTrue();
            last = actual;
        }

        // check delete
        mvc.perform(MockMvcRequestBuilders.delete("/api/event/" + last.getId()))
                .andExpect(status().isOk());

        // check if deleted
        mvc.perform(MockMvcRequestBuilders.get("/api/event/" + last.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.EVENT_NOT_FOUND.name()));

        // get all eventTypes
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/type"))
                .andExpect(status().isOk())
                .andReturn();
        List<EventTypeSO> eventTypes = mapListFromJson(mvcResult.getResponse().getContentAsString(), EventTypeSO.class);
        assertThat(eventTypes.size()).isEqualTo(11);

        // just random check returned data
        assertThat(eventTypes.get(0).getCode()).isEqualTo("WATER");
        assertThat(eventTypes.get(3).getCode()).isEqualTo("TRIM");
        assertThat(eventTypes.get(7).getCode()).isEqualTo("CUTTING");

        assertThat(eventTypes.get(0).getSchedulable()).isTrue();
        assertThat(eventTypes.get(3).getSchedulable()).isFalse();
        assertThat(eventTypes.get(7).getSchedulable()).isFalse();

        // create new event for next test with different user
        createEventSO = easyRandom.nextObject(CreateEventSO.class);
        createEventSO.getPlant().setId(1L);
        createEventSO.getType().setId(1L);

        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createEventSO)))
                .andExpect(status().isCreated())
                .andReturn();

        EventSO createdEventSO = mapFromJson(mvcResult.getResponse().getContentAsString(), EventSO.class);

        super.setAuthentication("user3");

        // try to update event of different user
        mvc.perform(MockMvcRequestBuilders.put("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createdEventSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.EVENT_NOT_FOUND.name()));

        // try get all events of plant of different user
        mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=0&pageSize=10"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        // try get single event of different user
        mvc.perform(MockMvcRequestBuilders.get("/api/event/" + createdEventSO.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.EVENT_NOT_FOUND.name()));

        // try delete event of different user
        mvc.perform(MockMvcRequestBuilders.delete("/api/event/" + createdEventSO.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.EVENT_NOT_FOUND.name()));
    }
}
