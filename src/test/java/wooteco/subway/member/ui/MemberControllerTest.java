package wooteco.subway.member.ui;

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
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.ui.dto.MemberRequest;
import wooteco.subway.member.ui.dto.MemberResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.docs.ApiDocumentUtils.getDocumentRequest;
import static wooteco.docs.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(controllers = MemberController.class)
@AutoConfigureRestDocs
class MemberControllerTest {
    private static final MemberRequest memberRequest
            = new MemberRequest("email@email.com", "1234", 10);
    private static final MemberResponse memberResponse
            = new MemberResponse(1L, "email@email.com", 10);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    @Test
    void createMember() throws Exception {
        given(memberService.createMember(any(MemberRequest.class)))
                .willReturn(memberResponse);

        ResultActions perform = mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/1"));

        perform.andDo(document("member - create",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("email").type(STRING).description("이메일"),
                        fieldWithPath("password").type(STRING).description("패스워드"),
                        fieldWithPath("age").type(NUMBER).description("나이")
                )));
    }

    @Test
    void findMemberOfMine() throws Exception {
        given(memberService.findMember(any(LoginMember.class))).willReturn(memberResponse);
        given(authService.findMemberByToken(anyString())).willReturn(
                new LoginMember(memberResponse.getId(), memberResponse.getEmail(), memberResponse.getAge())
        );

        ResultActions perform = mockMvc.perform(
                get("/members/me")
                        .header("Authorization", "Bearer test"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberResponse)));

        perform.andDo(
                document("member - find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("멤버 id"),
                                fieldWithPath("email").type(STRING).description("멤버 email"),
                                fieldWithPath("age").type(NUMBER).description("멤버 나이")
                        )
                )
        );

    }

    @Test
    void updateMemberOfMine() throws Exception {
        doNothing().when(memberService).updateMember(any(LoginMember.class), any(MemberRequest.class));
        given(authService.findMemberByToken(anyString())).willReturn(
                new LoginMember(memberResponse.getId(), memberResponse.getEmail(), memberResponse.getAge())
        );

        ResultActions perform = mockMvc.perform(
                put("/members/me")
                        .header(AUTHORIZATION, "Bearer {TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isOk());

        perform.andDo(document("member - update",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("bearer token")
                ),
                requestFields(
                        fieldWithPath("email").type(STRING).description("멤어 email"),
                        fieldWithPath("password").type(STRING).description("멤버 password"),
                        fieldWithPath("age").type(NUMBER).description("멤버 나이")
                )
        ));

    }

    @Test
    void deleteMemberOfMine() throws Exception {
        doNothing().when(memberService).deleteMember(any(LoginMember.class));
        given(authService.findMemberByToken(anyString())).willReturn(
                new LoginMember(memberResponse.getId(), memberResponse.getEmail(), memberResponse.getAge())
        );

        ResultActions perform = mockMvc.perform(
                delete("/members/me")
                        .header(AUTHORIZATION, "Bearer {TOKEN}"))
                .andExpect(status().isNoContent());

        perform.andDo(document("member - delete",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("bearer token")
                )
        ));

    }
}