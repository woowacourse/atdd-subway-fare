package wooteco.subway.member.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.EmailCheckRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.MemberNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 24;

    @InjectMocks
    private MemberService memberService;

    @Mock
    MemberDao memberDao;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("회원을 생성한다.")
    void createMember() {
        final MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(false);
        given(memberDao.insert(ArgumentMatchers.any(Member.class))).willReturn(new Member(1L, EMAIL,PASSWORD,AGE));

        final MemberResponse memberResponse = memberService.createMember(memberRequest);
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(AGE);
        assertThat(memberResponse.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("이메일이 이미 존재하는 회원을 생성할 시 중복 예외를 던진다.")
    void createMemberWithExistedEmail() {
        final MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(true);

        assertThatThrownBy(() -> memberService.createMember(memberRequest))
                .isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("로그인한 회원의 이메일로 회원을 찾는다.")
    void findMember() {
        final LoginMember loginMember = new LoginMember(1L, EMAIL, AGE, true);
        given(memberDao.findByEmail(EMAIL)).willReturn(new Member(1L, EMAIL,PASSWORD,AGE));

        final MemberResponse memberResponse = memberService.findMember(loginMember);
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(AGE);
        assertThat(memberResponse.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인하지 않은 회원의 이메일로 회원을 찾을 시 예외를 던진다.")
    void findMemberWithNotLogin() {
        final LoginMember loginMember = new LoginMember(false);

        assertThatThrownBy(() -> memberService.findMember(loginMember))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("회원 정보를 수정한다.")
    void updateMember() {
        final LoginMember loginMember = new LoginMember(1L, EMAIL, AGE, true);
        final MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(true);
        given(memberDao.findByEmail(EMAIL)).willReturn(new Member(1L, EMAIL,PASSWORD,AGE));

        assertThatCode(() -> memberService.updateMember(loginMember, memberRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효하지 않은 이메일을 가진 회원 정보를 수정할 경우 예외를 던진다.")
    void updateMemberWithInvalidEmail() {
        final LoginMember loginMember = new LoginMember(1L, EMAIL, AGE, true);
        final MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(false);

        assertThatThrownBy(() -> memberService.updateMember(loginMember, memberRequest))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이메일로 회원 정보를 삭제한다.")
    void deleteMember() {
        final LoginMember loginMember = new LoginMember(1L, EMAIL, AGE, true);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(true);
        given(memberDao.findByEmail(EMAIL)).willReturn(new Member(1L, EMAIL,PASSWORD,AGE));

        assertThatCode(() -> memberService.deleteMember(loginMember))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효하지 않은 이메일로 회원 정보를 삭제할 경우 예외를 던진다.")
    void deleteMemberWithInvalidEmail() {
        final LoginMember loginMember = new LoginMember(1L, EMAIL, AGE, true);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(false);

        assertThatThrownBy(() -> memberService.deleteMember(loginMember))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("이메일 중복여부를 확인하고, 중복이 아닌 경우 리턴하지 않는다.")
    void confirmEmailIsValid() {
        final EmailCheckRequest emailCheckRequest = new EmailCheckRequest(EMAIL);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(false);

        assertThatCode(() -> memberService.confirmEmailIsValid(emailCheckRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이메일 중복여부를 확인하고, 중복인 경우 예외를 던진다.")
    void confirmEmailIsValidThrowException() {
        final EmailCheckRequest emailCheckRequest = new EmailCheckRequest(EMAIL);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(true);

        assertThatThrownBy(() -> memberService.confirmEmailIsValid(emailCheckRequest))
                .isInstanceOf(DuplicatedException.class);
    }
}