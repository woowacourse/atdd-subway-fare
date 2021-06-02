package wooteco.subway.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("멤버 관련 테스트")
@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest extends ControllerTest {
    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    @DisplayName("생성 - 성공")
    @Test
    public void create() throws Exception {
        MemberRequest memberRequest = new MemberRequest("email@email.com", "password", 20);
        MemberResponse memberResponse = new MemberResponse(1L, "email@email.com", 20);
        given(memberService.createMember(any(MemberRequest.class))).willReturn(memberResponse);

        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(memberRequest))
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/" + 1L))
                .andDo(print())
                .andDo(document("member-create"));
    }

    @DisplayName("수정 - 성공")
    @Test
    public void update() throws Exception {
        LoginMember loginMember = new LoginMember(1L, "email@email.com", 20);
        MemberRequest memberRequest = new MemberRequest("email@email.com", "password", 20);
        given(authService.findMemberByToken("secrettokentoken")).willReturn(loginMember);

        mockMvc.perform(put("/members/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer secrettokentoken")
                .content(objectMapper.writeValueAsString(memberRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("members-updateme"));
    }

    @DisplayName("삭제 - 성공")
    @Test
    public void deleteMember() throws Exception {
        LoginMember loginMember = new LoginMember(1L, "email@eamil.com", 12);
        given(authService.findMemberByToken("secrettokentoken")).willReturn(loginMember);
        willDoNothing().given(memberService).deleteMember(loginMember);

        mockMvc.perform(delete("/members/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer secrettokentoken"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("members-deleteme"));
    }

    @DisplayName("조회 - 성공")
    @Test
    public void find() throws Exception {
        LoginMember loginMember = new LoginMember(1L, "email@email.com", 20);
        MemberResponse memberResponse = new MemberResponse(1L, "email@email.com", 20);
        given(authService.findMemberByToken("secrettokentoken")).willReturn(loginMember);
        given(memberService.findMember(loginMember)).willReturn(memberResponse);

        mockMvc.perform(get("/members/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer secrettokentoken"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberResponse)))
                .andDo(print())
                .andDo(document("members-findme"));
    }
}
