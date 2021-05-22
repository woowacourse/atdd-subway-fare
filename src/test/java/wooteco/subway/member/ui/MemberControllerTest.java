package wooteco.subway.member.ui;

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
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("유저 생성 - 성공")
    public void createMember() throws Exception {
        //given
        String email = "test@test.com";
        int age = 20;
        MemberRequest memberRequest = new MemberRequest(email, "1234", age);
        MemberResponse memberResponse = new MemberResponse(1L, email, age);

        given(memberService.createMember(any(MemberRequest.class)))
            .willReturn(memberResponse);

        mockMvc.perform(post("/members")
            .content(objectMapper.writeValueAsString(memberRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andDo(document("member-create"));
    }

    @Test
    @DisplayName("현재 유저 조 - 성공")
    public void findMember() throws Exception {
        //given
        String token = "토큰이얌";
        Long id = 1L;
        String email = "test@test.com";
        int age = 20;

        given(authService.findMemberByToken(token))
            .willReturn(new LoginMember(id, email, age));

        given(memberService.findMember(any(LoginMember.class)))
            .willReturn(new MemberResponse(1L, email, age));

        mockMvc.perform(get("/members/me")
            .header("Authorization", "Bearer " + token)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("email").value(email))
            .andExpect(jsonPath("age").value(age))
            .andDo(document("member-find"));
    }

    @Test
    @DisplayName("유저 수정 - 성공")
    public void updateMember() throws Exception {
        //given
        String token = "토큰이얌";
        Long id = 1L;
        String email = "test@test.com";
        int age = 20;
        MemberRequest memberRequest = new MemberRequest(email, "1234", age);

        given(authService.findMemberByToken(token))
            .willReturn(new LoginMember(id, email, age));

        mockMvc.perform(put("/members/me")
            .header("Authorization", "Bearer " + token)
            .content(objectMapper.writeValueAsString(memberRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(document("member-update"));
    }

    @Test
    @DisplayName("유저 삭제 - 성공")
    void deleteMember() throws Exception {
        //given
        String token = "이것은토큰입니다";
        long id = 1L;
        String email = "test@email.com";
        int age = 20;
        given(authService.findMemberByToken(token))
            .willReturn(new LoginMember(id, email, age));

        mockMvc.perform(delete("/members/me")
            .header("Authorization", "Bearer " + token)
        )
            .andExpect(status().isNoContent())
            .andDo(document("member-delete"));
    }
}