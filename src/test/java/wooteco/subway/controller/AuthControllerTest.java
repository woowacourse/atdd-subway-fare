package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.ui.AuthController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증 관련 테스트")
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest extends ControllerTest {
    public static final TokenRequest TOKEN_REQUEST = new TokenRequest("email@email.com", "password");
    public static final TokenResponse TOKEN_RESPONSE = new TokenResponse("abcdef");

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {
        String json = new ObjectMapper().writeValueAsString(TOKEN_REQUEST);

        given(authService.login(any(TokenRequest.class))).willReturn(TOKEN_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/login/token")
                .content(json)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken")
                        .value(TOKEN_RESPONSE.getAccessToken()))
                .andDo(print())
                .andDo(document("auth-token",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("EMAIL"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("PASSWORD")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("TOKEN")
                        )
                ));
    }
}