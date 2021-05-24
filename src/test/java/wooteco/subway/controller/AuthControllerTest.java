package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.ui.AuthController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증 관련 테스트")
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest extends ControllerTest {
    @MockBean
    AuthService authService;

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {
        TokenRequest tokenRequest = new TokenRequest("email@email.com", "password");
        String json = new ObjectMapper().writeValueAsString(tokenRequest);
        TokenResponse tokenResponse = new TokenResponse("abcdef");
        given(authService.login(any(TokenRequest.class))).willReturn(tokenResponse);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/login/token")
                .content(json)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken")
                        .value(tokenResponse.getAccessToken()))
                .andDo(print())
                .andDo(document("auth-token"));
    }
}