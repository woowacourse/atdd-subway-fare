package wooteco.subway.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import wooteco.subway.auth.ui.AuthenticationInterceptor;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.dto.ValidateEmailRequest;
import wooteco.subway.member.ui.MemberController;

@WebMvcTest(controllers = MemberController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    private AuthService authService;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원 생성")
    public void createMember() throws Exception {
        // given
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        MemberRequest memberRequest = new MemberRequest("email@gmail.com", "password", 20);
        given(memberService.createMember(any(MemberRequest.class)))
            .willReturn(new MemberResponse(1L, "email@gmail.com", 20));

        // then
        mockMvc.perform(post("/api/members")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(memberRequest)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("createMember",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("내 정보 조회")
    public void findMemberOfMine() throws Exception {
        // given
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(authService.findMemberByToken(null))
            .willReturn(new Member(1L, "email@email.com", "password", 20));
        given(memberService.findMember(any(Member.class)))
            .willReturn(new MemberResponse(1L, "email@gmail.com", 20));

        // then
        mockMvc.perform(get("/api/members/me")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("findMemberOfMine",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("내 정보 수정")
    public void updateMemberOfMine() throws Exception {
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        MemberRequest memberRequest = new MemberRequest("email@email.com", "password", 20);
        given(authService.findMemberByToken(null))
            .willReturn(new Member(1L, "email@email.com", "password", 20));

        // then
        mockMvc.perform(put("/api/members/me")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(memberRequest)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("updateMemberOfMine",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("내 정보 삭제")
    public void deleteMemberOfMine() throws Exception {
        // given
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(authService.findMemberByToken(null))
            .willReturn(new Member(1L, "email@email.com", "password", 20));

        // then
        mockMvc.perform(delete("/api/members/me")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("deleteMemberOfMine",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("이메일 중복 검증")
    public void validateMember() throws Exception {
        // given
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(authService.findMemberByToken(null))
            .willReturn(new Member(1L, "email@email.com", "password", 20));
        ValidateEmailRequest emailRequest = new ValidateEmailRequest("email@email.com");

        // then
        mockMvc.perform(get("/api/members/check-validation")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(emailRequest)))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(document("validateMemberEmail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
}
