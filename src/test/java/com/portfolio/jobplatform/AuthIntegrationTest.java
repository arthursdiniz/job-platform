package com.portfolio.jobplatform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class AuthIntegrationTest {
    @Autowired MockMvc mvc;
    @Test void registersCandidateAndReturnsJwtWithoutPassword() throws Exception {
        mvc.perform(post("/api/auth/register/candidate").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"Ana Dev","email":"ana@example.com","password":"Password123!","headline":"Java Developer","experienceLevel":"JUNIOR"}
                        """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer")).andExpect(jsonPath("$.user.email").value("ana@example.com"))
                .andExpect(jsonPath("$.user.password").doesNotExist());
    }
    @Test void protectedEndpointRequiresAuthentication() throws Exception {
        mvc.perform(get("/api/candidates/me/dashboard")).andExpect(status().isUnauthorized());
    }
}
