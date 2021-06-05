package wooteco.subway.member.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.ServiceTest;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.dto.EmailExistsResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceTest extends ServiceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;

    @Autowired
    private MemberService memberService;

    @Test
    void createMember() {
        MemberResponse response = 멤버_생성_요청(EMAIL, PASSWORD, AGE);
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @Test
    void findMember() {
        MemberResponse response = 멤버_생성_요청(EMAIL, PASSWORD, AGE);

        LoginUser loginUser = new LoginUser(response.getId(), response.getEmail(), response.getAge());
        MemberResponse foundMember = memberService.findMember(loginUser);

        회원_정보_조회됨(foundMember, EMAIL, AGE);
    }

    @Test
    void updateMember() {
        String emailToUpdate = EMAIL + "other";
        MemberResponse response = 멤버_생성_요청(EMAIL, PASSWORD, AGE);
        LoginUser loginUser = new LoginUser(response.getId(), response.getEmail(), response.getAge());

        MemberRequest request = new MemberRequest(emailToUpdate, PASSWORD, AGE);
        memberService.updateMember(loginUser, request);

        MemberResponse updatedMember = memberService.findMember(loginUser);
        회원_정보_조회됨(updatedMember, emailToUpdate, AGE);
    }

    @Test
    void updateMemberToExistingEmail() {
        String existingEmail = "existing@email.com";
        멤버_생성_요청(existingEmail, PASSWORD, AGE);
        MemberResponse response = 멤버_생성_요청(EMAIL, PASSWORD, AGE);

        LoginUser loginUser = new LoginUser(response.getId(), response.getEmail(), response.getAge());
        MemberRequest request = new MemberRequest(existingEmail, PASSWORD, AGE);
        assertThatThrownBy(() ->
                memberService.updateMember(loginUser, request)).isInstanceOf(MemberException.class);
    }

    @Test
    void deleteMember() {
        MemberResponse response = 멤버_생성_요청(EMAIL, PASSWORD, AGE);

        LoginUser loginUser = new LoginUser(response.getId(), response.getEmail(), response.getAge());

        memberService.deleteMember(loginUser);
        assertThatThrownBy(() -> memberService.findMember(loginUser)).isInstanceOf(MemberException.class);
    }

    @Test
    void deleteNotExistingMember() {
        LoginUser fakeUser = new LoginUser(1000L, "notEmail", 999);

        assertThatThrownBy(() -> memberService.deleteMember(fakeUser)).isInstanceOf(MemberException.class);
    }

    @Test
    void isExistsEmail() {
        멤버_생성_요청(EMAIL, PASSWORD, AGE);

        assertThat(memberService.isExistingEmail(EMAIL))
                .usingRecursiveComparison()
                .isEqualTo(new EmailExistsResponse(true));
    }

    @Test
    void isExistsEmailFalse() {
        assertThat(memberService.isExistingEmail(EMAIL))
                .usingRecursiveComparison()
                .isEqualTo(new EmailExistsResponse(false));
    }

    private MemberResponse 멤버_생성_요청(String email, String password, int age) {
        MemberRequest request = new MemberRequest(email, password, age);
        return memberService.createMember(request);
    }

    private void 회원_정보_조회됨(MemberResponse memberResponse, String email, int age) {
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }
}
