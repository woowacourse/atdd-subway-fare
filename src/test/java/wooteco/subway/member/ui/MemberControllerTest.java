package wooteco.subway.member.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@WebMvcTest(controllers = MemberController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;
    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("유저 생성 - 성공")
    @Test
    public void createMember() throws Exception {
        //given
        String email = "test@test.com";
        int age = 20;
        MemberRequest memberRequest = new MemberRequest(email, "password", age);
        MemberResponse memberResponse = new MemberResponse(1L, email, age);

        given(memberService.createMember(any(MemberRequest.class))).willReturn(memberResponse);

        //when
        mockMvc.perform(post("/api/members")
            .content(objectMapper.writeValueAsString(memberRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            // then
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andDo(document("member-create"));
    }

    @DisplayName("현재 유저 조회 - 성공")
    @Test
    public void findMember() throws Exception {
        //given
        String token = "이것은토큰";
        String email = "test@test.com";
        int age = 20;
        long id = 1L;

        given(authService.findMemberByToken(token))
            .willReturn(new LoginMember(id, email, age));

        given(memberService.findMember(any(LoginMember.class)))
            .willReturn(new MemberResponse(id, email, age));

        //when
        mockMvc.perform(get("/api/members/me")
            .header("Authorization", "Bearer " + token)
        )
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("age").value(age))
            .andExpect(jsonPath("email").value(email))
            .andDo(print())
            .andDo(document("member-find"));
    }

    @DisplayName("현재 유저 수정 - 성공")
    @Test
    public void updateMember() throws Exception {
        //given
        String token = "이것은토큰";
        String email = "test@test.com";
        int age = 20;
        int newAge = 21;
        long id = 1L;

        MemberRequest memberRequest = new MemberRequest(email, "password", newAge);
        MemberResponse memberResponse = new MemberResponse(1L, email, newAge);

        given(authService.findMemberByToken(token))
            .willReturn(new LoginMember(id, email, age));

        given(memberService.updateMember(any(LoginMember.class), any(MemberRequest.class)))
            .willReturn(memberResponse);

        //when
        mockMvc.perform(put("/api/members/me")
            .content(objectMapper.writeValueAsString(memberRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + token)
        )
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("email").value(email))
            .andExpect(jsonPath("age").value(newAge))
            .andDo(print())
            .andDo(document("member-update"));
    }

    @DisplayName("현재 유저 삭제 - 성공")
    @Test
    void deleteMember() throws Exception {
        //given
        String token = "이것은토큰입니다";
        long id = 1L;
        String email = "test@email.com";
        int age = 20;
        given(authService.findMemberByToken(token))
            .willReturn(new LoginMember(id, email, age));

        // when
        mockMvc.perform(delete("/api/members/me")
            .header("Authorization", "Bearer " + token)
        )
            // then
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("member-delete"));
    }
}
