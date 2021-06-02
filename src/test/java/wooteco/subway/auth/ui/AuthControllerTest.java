package wooteco.subway.auth.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import wooteco.subway.auth.dto.TokenRequest;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(AuthController.class)
            .alwaysExpect(MockMvcResultMatchers.status().isOk())
            .build();
    }

    @Test
    void loginSuccess() throws Exception{
        String content = objectMapper.writeValueAsString(new TokenRequest("email@email.com", "password"));

        this.mockMvc.perform(post("/api/login/token")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk());
    }
}