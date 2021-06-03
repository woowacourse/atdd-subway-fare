package wooteco.subway.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.AnonymousUser;
import wooteco.subway.auth.domain.LoginUser;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicateEmailException;
import wooteco.subway.member.exception.EmailUpdateTrialException;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class MemberServiceTest {
    private static final String EMAIL = "joel@joel";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 10;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("MemberRequest로 멤버 생성 요청을 하면 MemberResponse를 반환한다")
    void createMember() {
        final MemberResponse response = memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));

        assertThat(response.getEmail()).isEqualTo(EMAIL);
        assertThat(response.getAge()).isEqualTo(10);
    }

    @Test
    @DisplayName("EmailRequest를 통해 등록 가능한 이메일인지 확인하며, 불가능하다면 예외를 던진다.")
    void checkPossibleEmail() {
        final EmailRequest emailRequest = new EmailRequest(EMAIL);
        assertThatCode(() -> memberService.checkPossibleEmail(emailRequest))
                .doesNotThrowAnyException();

        memberService.createMember(new MemberRequest(EMAIL, "password", 25));
        assertThatThrownBy(() -> memberService.checkPossibleEmail(emailRequest))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("인가된 User에 해당하는 Member를 DB에서 조회해 온다")
    void findMember() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));

        final MemberResponse memberResponse = memberService.findMember(new LoginUser(1L, EMAIL, AGE));
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(AGE);
    }

    @Test
    @DisplayName("익명 사용자는 Member를 DB에서 조회해 올 수 없다")
    void findMemberAnonymous() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));

        assertThatThrownBy(() -> memberService.findMember(new AnonymousUser()))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("로그인 사용자라도 DB에 Email이 등록되있지 않다면 예외를 반환한다.")
    void findMemberWrongUser() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));

        assertThatThrownBy(() -> memberService.findMember(new LoginUser(1L, "wrong@wrong", AGE)))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("인가된 User에 해당하는 Member를 DB에서 조회해 수정 작업을 요청한다")
    void updateMember() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));

        final LoginUser loginUser = new LoginUser(1L, EMAIL, AGE);
        memberService.updateMember(loginUser, new MemberRequest(EMAIL, "newpassword", 25));

        final MemberResponse member = memberService.findMember(loginUser);
        assertThat(member.getEmail()).isEqualTo(EMAIL);
        assertThat(member.getAge()).isEqualTo(25);
    }

    @Test
    @DisplayName("인가된 User에 해당하는 Member를 DB에서 조회해 수정 작업을 요청 시 Email은 수정할 수 없음")
    void updateMemberCannotChangeEmail() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));

        final LoginUser loginUser = new LoginUser(1L, EMAIL, AGE);
        assertThatThrownBy(() -> memberService.updateMember(loginUser, new MemberRequest("joel@newEmail", "newpassword", 25)))
                .isInstanceOf(EmailUpdateTrialException.class);
    }
    
    @Test
    void deleteMember() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));

        final LoginUser loginUser = new LoginUser(1L, EMAIL, AGE);
        memberService.deleteMember(loginUser);

        assertThatThrownBy(() -> memberService.findMember(loginUser))
                .isInstanceOf(AuthorizationException.class);
    }
}