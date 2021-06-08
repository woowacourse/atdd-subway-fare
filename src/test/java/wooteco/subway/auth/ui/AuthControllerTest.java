package wooteco.subway.auth.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.infrastructure.LoginInterceptor;

@WebMvcTest(controllers = AuthController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class AuthControllerTest {

    @MockBean
    private AuthService authService;
    @MockBean
    private LoginInterceptor loginInterceptor;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("로그인 - 성공")
    @Test
    public void login() throws Exception {
        //given
        TokenRequest tokenRequest = new TokenRequest("test@test.com", "password");
        TokenResponse tokenResponse = new TokenResponse("아주강력한비밀번호");
        given(authService.login(any())).willReturn(tokenResponse);

        //when
        mockMvc.perform(post("/api/login/token")
            .content(objectMapper.writeValueAsString(tokenRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("accessToken").value(tokenResponse.getAccessToken()))
            .andDo(print())
            .andDo(document("auth-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }
}
