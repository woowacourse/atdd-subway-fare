package wooteco.subway.auth.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.ui.dto.TokenRequest;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.infrastructure.dao.MemberDao;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.docs.ApiDocumentUtils.getDocumentRequest;
import static wooteco.docs.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(controllers = {AuthController.class, AuthService.class})
@AutoConfigureRestDocs
class AuthControllerTest {
    private static final String EMAIL = "email@emai.com";
    private static final String PASSWORD = "1234";
    private static final String TOKEN = "token";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberDao memberDao;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void login() throws Exception {
        given(memberDao.findByEmail(anyString())).willReturn(Optional.of(new Member(1L, EMAIL, PASSWORD, 10)));
        given(jwtTokenProvider.createToken(anyString())).willReturn(TOKEN);
        given(memberDao.existsByEmail(anyString())).willReturn(true);


        ResultActions result = mockMvc.perform(
                post("/login/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TokenRequest(EMAIL, PASSWORD)
                        ))
        );

        result.andExpect(status().isOk())
                .andDo(document("auth - login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(STRING).description("이메일"),
                                fieldWithPath("password").type(STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(STRING).description("인증 토큰")
                        )
                ));
    }
}