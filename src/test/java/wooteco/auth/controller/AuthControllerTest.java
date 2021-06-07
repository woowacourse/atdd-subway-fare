package wooteco.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.auth.service.AuthService;
import wooteco.auth.util.JwtTokenProvider;
import wooteco.auth.web.api.AuthController;
import wooteco.auth.web.dto.request.TokenRequest;
import wooteco.auth.web.dto.response.TokenResponse;

@WebMvcTest(controllers = AuthController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그인 - 성공")
    public void login() throws Exception {
        final TokenResponse tokenResponse = new TokenResponse("이것은토큰비밀번호");
        given(authService.login(any())).willReturn(tokenResponse);
        final TokenRequest tokenRequest = new TokenRequest("test@test.com", "password");

        mockMvc.perform(post("/api/login/token")
            .content(objectMapper.writeValueAsString(tokenRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("accessToken").value(tokenResponse.getAccessToken()))
            .andDo(document("auth-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

}
