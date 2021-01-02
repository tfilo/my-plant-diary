package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.so.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class EventControllerIT extends BaseIntegrationTest {

    @Test
    public void eventBasicTest() throws Exception {
        super.setAuthentication("user");

        // test create event on non existing plant and non existing event type
        CreateEventSO createEventSO = easyRandom.nextObject(CreateEventSO.class);
        createEventSO.getPlant().setId(100000L);
        createEventSO.getType().setId(100000L);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createEventSO))).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.EVENT_TYPE_NOT_FOUND.name());

        // test create event on non existing plant but existing event type, minimal data
        createEventSO.getType().setId(1L);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createEventSO))).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.PLANT_NOT_FOUND.name());

        // test create event on existing plant and existing event, minimal data
        createEventSO.getPlant().setId(1L);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createEventSO))).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
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
                .content(mapToJson(eventSO))).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        EventSO updatedEventSO = mapFromJson(mvcResult.getResponse().getContentAsString(), EventSO.class);

        // check if correctly plant name and type name and schedulable wasn't modified
        assertThat(eventSO).usingRecursiveComparison().isNotEqualTo(updatedEventSO);
        // check if other fields was modified correctly
        assertThat(eventSO).usingRecursiveComparison().ignoringFields("type.code", "type.schedulable", "plant.name").isEqualTo(updatedEventSO);

        // test get of non existing record
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/100000")).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.EVENT_NOT_FOUND.name());

        // test get of existing record
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/" + updatedEventSO.getId())).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        eventSO = mapFromJson(mvcResult.getResponse().getContentAsString(), EventSO.class);

        assertThat(updatedEventSO).usingRecursiveComparison().isEqualTo(eventSO);

        // get All tests
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/100000?page=0&pageSize=10")).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.PLANT_NOT_FOUND.name());

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=0&pageSize=10")).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
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
                    .content(mapToJson(createEventSO))).andReturn();
        }

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=0&pageSize=10")).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
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

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=1&pageSize=10")).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        events = mapPagedResponse(mvcResult.getResponse().getContentAsString(), EventSO.class);

        while (iterator.hasNext()) { // check if ordered by date
            EventSO actual = iterator.next();
            assertThat(last.getDateTime().isAfter(actual.getDateTime())).isTrue();
            last = actual;
        }

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=2&pageSize=10")).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        events = mapPagedResponse(mvcResult.getResponse().getContentAsString(), EventSO.class);

        while (iterator.hasNext()) { // check if ordered by date
            EventSO actual = iterator.next();
            assertThat(last.getDateTime().isAfter(actual.getDateTime())).isTrue();
            last = actual;
        }

        // check delete
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/event/" + last.getId())).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        // check if deleted
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/" + last.getId())).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.EVENT_NOT_FOUND.name());

        // get all eventTypes
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/type")).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
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
                .content(mapToJson(createEventSO))).andReturn();

        EventSO createdEventSO = mapFromJson(mvcResult.getResponse().getContentAsString(), EventSO.class);

        super.setAuthentication("user3");

        // try to update event of different user
        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(createdEventSO))).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.EVENT_NOT_FOUND.name());

        // try get all events of plant of different user
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/all/1?page=0&pageSize=10")).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.PLANT_NOT_FOUND.name());

        // try get single event of different user
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/event/" + createdEventSO.getId())).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.EVENT_NOT_FOUND.name());

        // try delete event of different user
        mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/api/event/" + createdEventSO.getId())).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.EVENT_NOT_FOUND.name());
    }
}
