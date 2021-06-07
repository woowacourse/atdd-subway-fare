package wooteco.subway.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

@MockitoSettings
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private MemberDao memberDao;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private String email;
    private String password;
    private Member member;

    @DisplayName("로그인에 성공한다.")
    @Test
    void loginTestSuccess() {
        // given
        email = "pkeugine@gmail.com";
        password = "password";
        member = new Member(1L, email, password, 20);
        String accessToken = "로키의토큰";
        TokenRequest tokenRequest = new TokenRequest(email, password);

        when(memberDao.findByEmail(tokenRequest.getEmail())).thenReturn(member);
        when(jwtTokenProvider.createToken(tokenRequest.getEmail())).thenReturn(accessToken);

        // when
        TokenResponse tokenResponse = authService.login(tokenRequest);

        // then
        assertThat(tokenResponse.getAccessToken()).isEqualTo(accessToken);
    }

    @DisplayName("로그인에 실패한다. - 비밀번호가 다름")
    @Test
    void loginTestFail_wrongPassword() {
        // given
        email = "pkeugine@gmail.com";
        password = "password";
        String wrongPassword = "password2";
        Member member = new Member(1L, email, password, 20);
        TokenRequest tokenRequest = new TokenRequest(email, wrongPassword);

        when(memberDao.findByEmail(tokenRequest.getEmail())).thenReturn(member);

        // when // then
        assertThatThrownBy(() -> authService.login(tokenRequest))
                .isExactlyInstanceOf(AuthorizationException.class);
    }

    @DisplayName("토큰으로 멤버를 찾는다")
    @Test
    void findMemberByTokenSuccess() {
        // given
        email = "pkeugine@gmail.com";
        password = "password";
        member = new Member(1L, email, password, 20);

        String token = "로키의토큰";

        when(jwtTokenProvider.getPayload(token)).thenReturn(email);
        when(memberDao.findByEmail(email)).thenReturn(member);

        // when
        LoginMember loginMember = authService.findMemberByToken(token);

        // then
        assertThat(loginMember).isNotNull();
        assertThat(loginMember).extracting("id").isEqualTo(1L);
        assertThat(loginMember).extracting("email").isEqualTo(email);
        assertThat(loginMember).extracting("age").isEqualTo(20);
    }

    @DisplayName("토큰 검증 성공한다.")
    @Test
    void validateTokenSuccess() {
        // given
        String token = "로키의토오큰";

        when(jwtTokenProvider.validateToken(token)).thenReturn(true);

        // when
        authService.validateToken(token);

        // then
        verify(jwtTokenProvider).validateToken(token);
    }

    @DisplayName("토큰 검증 실패한다. - 유효하지 않은 토큰")
    @Test
    void validateTokenFail() {
        // given
        String token = "로키의토오큰";

        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        // when // then
        assertThatThrownBy(() -> authService.validateToken(token))
                .isExactlyInstanceOf(AuthorizationException.class);
    }


}