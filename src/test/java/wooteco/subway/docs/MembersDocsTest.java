package wooteco.subway.docs;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.MemberRequest;

public class MembersDocsTest extends DocsTest {

    @Test
    @DisplayName("회원 가입")
    public void createMemberDocs() throws Exception {
        createMember().andDo(document("members/post",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이")
            )
        ));
    }

    @Test
    @DisplayName("로그인")
    public void loginWithToken() throws Exception {
        // given
        createMember();

        // when, then
        login().andDo(document("login/token/post",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
            ),
            responseFields(
                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("이메일")
            )
        ));
    }

    @Test
    @DisplayName("내 정보 조회")
    public void getMyMember() throws Exception {
        // given
        createMember();
        String tokenResponseJson = login().andReturn().getResponse().getContentAsString();
        TokenResponse tokenResponse = asObject(tokenResponseJson, TokenResponse.class);

        // when, then
        this.mockMvc.perform(get("/members/me")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("members/me/get",
                requestHeaders(
                    headerWithName("Authorization").description("Bearer - JWT 토큰")),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("index"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이")
                )
            ));
    }

    private ResultActions createMember() throws Exception {
        return this.mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new MemberRequest("qkrwotjd1445@naver.com", "1234", 20)))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    private ResultActions login() throws Exception {
        return this.mockMvc.perform(post("/login/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new TokenRequest("qkrwotjd1445@naver.com", "1234")))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
