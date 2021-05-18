package sk.filo.plantdiary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static io.github.benas.randombeans.FieldPredicates.named;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public abstract class BaseIntegrationTest {

    public static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:13.1-alpine"));
    public static GenericContainer maildev = new GenericContainer(DockerImageName.parse("djfarrelly/maildev")).withExposedPorts(25);

    protected static EasyRandom easyRandom;
    @Autowired
    public MockMvc mvc;
    @Autowired
    public Flyway flyway;
    @Autowired
    public WebApplicationContext webApplicationContext;
    @Autowired
    public ObjectMapper objectMapper;

    @BeforeAll
    public static void startContainers() {
        postgres.start();
        maildev.start();
        System.setProperty("PD_MAIL_PORT", maildev.getMappedPort(25).toString());
        System.setProperty("PD_DATASOURCE", postgres.getJdbcUrl());
        System.setProperty("PD_DATASOURCE_USER", postgres.getUsername());
        System.setProperty("PD_DATASOURCE_PASS", postgres.getPassword());
        System.setProperty("PD_ASYNC_EXECUTOR", "false"); // in test there must by synchronous task executor
        System.setProperty("PD_FLYWAY_CHECK_LOCATIONS", "classpath:/db/migration,classpath:/db/testdata");

        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(10, 15);
        parameters.collectionSizeRange(5, 5);
        parameters.excludeField(named("id"));
        parameters.randomize(LocalTime.class, () -> LocalTime.of(17, 00, 00));

        easyRandom = new EasyRandom(parameters);
    }

    @AfterAll
    public static void stopContainers() {
        postgres.stop();
    }

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel("sk.filo.plantdiary", LogLevel.DEBUG);
    }

    public String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    public <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    public <T> List<T> mapListFromJson(String json, Class<T> paramClazz) throws Exception {
        return getListFromNode(objectMapper.readTree(json), paramClazz);
    }

    public <T> Page<T> mapPagedResponse(String json, Class<T> paramClazz)
            throws IOException {
        JsonNode parent = objectMapper.readTree(json);
        return new PageImpl<>(
                getListFromNode(parent.get("content"), paramClazz),
                PageRequest.of(
                        parent.get("pageable").get("pageNumber").asInt(),
                        parent.get("pageable").get("pageSize").asInt()),
                parent.get("totalElements").asLong());
    }

    public <T> List<T> getListFromNode(JsonNode node, Class<T> clazz) throws IOException {
        List<T> content = new ArrayList<>();
        for (JsonNode val : node) {
            content.add(objectMapper.readValue(val.traverse(), clazz));
        }
        return content;
    }

    protected void setAuthentication(String user) {
        if (user == null) {
            SecurityContextHolder.clearContext();
        } else {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            user, null, null)
            );
        }
    }
}
