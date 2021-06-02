package wooteco.subway.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.ui.MemberController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("멤버 관련 테스트")
@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest extends ControllerTest {
    public static final MemberRequest MEMBER_REQUEST = new MemberRequest("email@email.com", "password", 20);
    public static final MemberResponse MEMBER_RESPONSE = new MemberResponse(1L, "email@email.com", 20);
    public static final LoginMember LOGIN_MEMBER = new LoginMember(1L, "email@email.com", 20);

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    @DisplayName("생성 - 성공")
    @Test
    public void create() throws Exception {
        given(memberService.createMember(any(MemberRequest.class))).willReturn(MEMBER_RESPONSE);

        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(MEMBER_REQUEST))
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/" + 1L))
                .andDo(print())
                .andDo(document("member-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("EMAIL"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("PASSWORD"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("AGE")
                        )
                ));
    }


    @DisplayName("조회 - 성공")
    @Test
    public void find() throws Exception {
        given(authService.findMemberByToken("secrettokentoken")).willReturn(LOGIN_MEMBER);
        given(memberService.findMember(LOGIN_MEMBER)).willReturn(MEMBER_RESPONSE);

        mockMvc.perform(get("/members/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer secrettokentoken"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(MEMBER_RESPONSE)))
                .andDo(print())
                .andDo(document("members-findme",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("EMAIL"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("AGE")
                        )
                ));
    }

    @DisplayName("수정 - 성공")
    @Test
    public void update() throws Exception {
        given(authService.findMemberByToken("secrettokentoken")).willReturn(LOGIN_MEMBER);

        mockMvc.perform(put("/members/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer secrettokentoken")
                .content(objectMapper.writeValueAsString(MEMBER_REQUEST))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("EMAIL"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("PASSWORD"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("AGE")
                        )
                ));
    }

    @DisplayName("삭제 - 성공")
    @Test
    public void deleteMember() throws Exception {
        given(authService.findMemberByToken("secrettokentoken")).willReturn(LOGIN_MEMBER);
        willDoNothing().given(memberService).deleteMember(LOGIN_MEMBER);

        mockMvc.perform(delete("/members/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer secrettokentoken"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("members-deleteme",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}
