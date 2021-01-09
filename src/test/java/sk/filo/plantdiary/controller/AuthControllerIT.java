package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.jwt.JwtToken;
import sk.filo.plantdiary.service.so.AuthSO;
import sk.filo.plantdiary.service.so.TokenSO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIT extends BaseIntegrationTest {

    @Autowired
    private JwtToken jwtToken;

    @Test
    public void authenticateUserTest() throws Exception {

        // TODO fix token https://jwt.io/#debugger-io
        // TEST correct user, correct password
        AuthSO so = new AuthSO();
        so.setUsername("username");
        so.setPassword("User123");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so)))
                .andExpect(status().isOk())
                .andReturn();

        TokenSO tokenSO = mapFromJson(mvcResult.getResponse().getContentAsString(), TokenSO.class);

        System.out.println(tokenSO.toString());

        assertThat(tokenSO.getType()).isEqualTo("Bearer");
        JwtToken.JwtUser tokenUser = jwtToken.parseToken(tokenSO.getToken());
        assertThat(tokenUser.getUsername()).isEqualTo("username");
        assertThat(tokenUser.getEnabled()).isTrue();

        // TEST correct user, incorrect password
        so.setUsername("username");
        so.setPassword("user123");
        mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.INVALID_CREDENTIALS.name()));

        // TEST incorrect user, correct password
        so.setUsername("username1");
        so.setPassword("User123");
        mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.INVALID_CREDENTIALS.name()));

        // TEST incorrect user, incorrect password
        so.setUsername("username1");
        so.setPassword("User223");
        mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.INVALID_CREDENTIALS.name()));

        // TEST disabled user
        so.setUsername("username2");
        so.setPassword("User123");
        mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(so)))
                .andExpect(status().isForbidden())
                .andExpect(status().reason(ExceptionCode.DISABLED_USER.name()));
    }
}
