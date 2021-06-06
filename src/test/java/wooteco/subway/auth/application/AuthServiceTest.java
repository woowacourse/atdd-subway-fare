package wooteco.subway.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.auth.domain.AnonymousUser;
import wooteco.subway.auth.domain.User;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberDao memberDao;

    @Test
    @DisplayName("TokenRequest를 통한 로그인이 인증 된다면, TokenRespone 반환")
    void login() {
        Mockito.when(memberDao.findByEmail("test@test"))
                .thenReturn(Optional.of(new Member(1L, "test@test", "test", 20)));

        assertThat(authService.login(new TokenRequest("test@test", "test")))
                .isInstanceOf(TokenResponse.class);
    }

    @Test
    @DisplayName("인가된 사용자가 요청하면, LoginUser를 반환한다.")
    void findMemberByTokenLoginUser() {
        final String token = "token";
        final String testEmail = "test@test";

        Mockito.when(jwtTokenProvider.validateToken(token)).thenReturn(true);
        Mockito.when(jwtTokenProvider.getPayload(token)).thenReturn(testEmail);
        Mockito.when(memberDao.findByEmail(testEmail))
                .thenReturn(Optional.of(new Member(1L, testEmail, "test", 20)));

        final User user = authService.findMemberByToken(token);
        assertThat(user.getEmail()).isEqualTo(testEmail);
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("인가되지 않은 사용자가 요청하면, AnonymousUser를 반환한다.")
    void findMemberByTokenAnonymousUser() {
        final String token = "token";

        Mockito.when(jwtTokenProvider.validateToken(token)).thenReturn(true);

        final User user = authService.findMemberByToken(token);
        assertThat(user).isInstanceOf(AnonymousUser.class);
    }
}