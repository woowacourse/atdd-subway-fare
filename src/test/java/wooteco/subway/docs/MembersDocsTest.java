package wooteco.subway.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.member.dto.MemberRequest;

public class MembersDocsTest extends DocsTest {

    @Test
    public void createMemberDocs() throws Exception {
        createMember().andDo(document("members-post",
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이")
            )
        ));
    }

    @Test
    public void loginWithToken() throws Exception {
        // given
        createMember();

        // when, then
        this.mockMvc.perform(post("/login/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new TokenRequest("qkrwotjd1445@naver.com", "1234")))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("login",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                ),
                responseFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING).description("이메일")
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
}
