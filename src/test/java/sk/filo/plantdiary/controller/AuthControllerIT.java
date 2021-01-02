package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.jwt.JwtToken;
import sk.filo.plantdiary.service.so.AuthSO;
import sk.filo.plantdiary.service.so.TokenSO;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthControllerIT extends BaseIntegrationTest {

    @Autowired
    private JwtToken jwtToken;

    @Test
    public void authenticateUserTest() throws Exception {

        // TEST correct user, correct password
        AuthSO so = new AuthSO();
        so.setUsername("user");
        so.setPassword("User123");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so))).andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        TokenSO tokenSO = mapFromJson(mvcResult.getResponse().getContentAsString(), TokenSO.class);

        assertThat(tokenSO.getType()).isEqualTo("Bearer");

        JwtToken.JwtUser tokenUser = jwtToken.parseToken(tokenSO.getToken());
        assertThat(tokenUser.getUsername()).isEqualTo("user");
        assertThat(tokenUser.getEnabled()).isTrue();

        // TEST correct user, incorrect password
        so.setUsername("user");
        so.setPassword("user123");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so))).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.INVALID_CREDENTIALS.name());

        // TEST incorrect user, correct password
        so.setUsername("user1");
        so.setPassword("User123");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so))).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.INVALID_CREDENTIALS.name());

        // TEST incorrect user, incorrect password
        so.setUsername("user1");
        so.setPassword("User223");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so))).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.INVALID_CREDENTIALS.name());

        // TEST disabled user
        so.setUsername("user2");
        so.setPassword("User123");
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so))).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(mvcResult.getResponse().getErrorMessage()).isEqualTo(ExceptionCode.DISABLED_USER.name());
    }
}
