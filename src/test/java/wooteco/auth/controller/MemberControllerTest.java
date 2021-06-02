package wooteco.auth.controller;

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
import wooteco.auth.domain.LoginMember;
import wooteco.auth.infrastructure.JwtTokenProvider;
import wooteco.auth.service.MemberService;
import wooteco.auth.web.MemberController;
import wooteco.auth.web.dto.request.MemberRequest;
import wooteco.auth.web.dto.response.MemberResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("유저 생성 - 성공")
    public void createMember() throws Exception{
        //given
        final String email = "test@email.com";
        final int age = 20;
        final MemberRequest memberRequest = new MemberRequest(email, "12341234", age);
        final MemberResponse memberResponse = new MemberResponse(1L, "test@email.com", age);

        given(memberService.createMember(any(MemberRequest.class)))
                .willReturn(memberResponse);

        mockMvc.perform(post("/api/members")
                .content(objectMapper.writeValueAsString(memberRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print())
                .andDo(document("member-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("현재 유저 조회")
    public void findMe() throws Exception{
        //given
        String token = "이것은토큰입니다";
        final long id = 1L;
        final String email = "test@email.com";
        final int age = 20;
        given(jwtTokenProvider.getPayload(token))
                .willReturn(Long.toString(id));
        given(memberService.findMember(any(LoginMember.class)))
                .willReturn(new MemberResponse(id, email, age));

        mockMvc.perform(get("/api/members/me")
                .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("age").value(age))
                .andDo(document("members-findme",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("현재 유저 수정 - 성공")
    public void updateMe() throws Exception{
        //given
        String token = "이것은토큰입니다";
        final long id = 1L;
        final String email = "test@email.com";
        final int age = 20;
        final int newAge = 29;
        given(jwtTokenProvider.getPayload(token))
                .willReturn(Long.toString(id));

        given(jwtTokenProvider.validateToken(token))
                .willReturn(true);

        given(memberService.updateMember(any(LoginMember.class), any(MemberRequest.class)))
                .willReturn(new MemberResponse(id, email, newAge));

        mockMvc.perform(put("/api/members/me")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(new MemberRequest(email, "12341234", newAge)))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("age").value(newAge))
                .andDo(document("members-updateme",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("현재 유저 삭제 - 성공")
    void deleteMe() throws Exception {
        //given
        String token = "이것은토큰입니다";
        long id = 1L;
        String email = "test@email.com";
        int age = 20;
        given(jwtTokenProvider.getPayload(token))
                .willReturn(Long.toString(id));

        given(jwtTokenProvider.validateToken(token))
                .willReturn(true);

        mockMvc.perform(
                delete("/api/members/me")
                        .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isNoContent())
                .andDo(document("members-deleteme",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("유저 중복 확인 - 성공")
    void duplicateMember() throws Exception {
        final String email = "test@email.com";
        given(memberService.existsMember(email)).willReturn(true);

        mockMvc.perform(get("/api/members?email="+email))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(document("member-duplicate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}