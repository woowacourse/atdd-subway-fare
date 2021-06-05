package wooteco.subway.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.TokenInvalidException;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String EMAIL = "email@email.com";
    private static final String TOKEN = "accessToken";
    private static final String PASSWORD = "password";
    private static final int AGE = 12;

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberDao memberDao;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("이메일을 받았을 때 로그인에 성공한다.")
    void login() {
        final TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        given(memberDao.isExistByEmail(tokenRequest.getEmail())).willReturn(true);
        given(memberDao.findByEmail(tokenRequest.getEmail())).willReturn(new Member(1L, EMAIL, PASSWORD, AGE));
        given(jwtTokenProvider.createToken(tokenRequest.getEmail())).willReturn(TOKEN);

        final TokenResponse tokenResponse = authService.login(tokenRequest);
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 이메일을 받았을 때 로그인에 실패한다.")
    void loginFailed() {
        final TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        given(memberDao.isExistByEmail(tokenRequest.getEmail())).willReturn(false);
        assertThatThrownBy(() -> authService.login(tokenRequest))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("토큰으로 멤버를 찾는다.")
    void findMemberByToken() {
        given(jwtTokenProvider.getPayload(TOKEN)).willReturn(EMAIL);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(true);
        given(memberDao.findByEmail(EMAIL)).willReturn(new Member(1L, EMAIL, PASSWORD, AGE));

        final Member member = authService.findMemberByToken(TOKEN);
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getEmail()).isEqualTo("email@email.com");
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 멤버를 찾을 시 예외를 발생한다.")
    void findMemberByInvalidToken() {
        given(jwtTokenProvider.getPayload(TOKEN)).willReturn(EMAIL);
        given(memberDao.isExistByEmail(EMAIL)).willReturn(false);

        assertThatThrownBy(() -> authService.findMemberByToken(TOKEN))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증 시 예외를 발생한다.")
    void validateInvalidToken() {
        given(jwtTokenProvider.validateToken(TOKEN)).willReturn(false);
        assertThatThrownBy(() -> authService.validateToken(TOKEN))
                .isInstanceOf(TokenInvalidException.class);
    }
}