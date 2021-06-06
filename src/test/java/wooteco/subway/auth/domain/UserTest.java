package wooteco.subway.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.auth.application.AuthorizationException;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("로그인 상태에 대해 LoginUser는 true를, AnonymousUser는 false를 반환한다.")
    void isLoggedIn() {
        User user = new LoginUser(1L, "joel@joel.com", 25);
        assertThat(user.isLoggedIn()).isTrue();
        
        user = new AnonymousUser();
        assertThat(user.isLoggedIn()).isFalse();
    }

    @Test
    @DisplayName("ID 반환 기능에 대해 LoginUser는 ID를, AnonymousUser는 예외를 반환한다.")
    void getId() {
        User loginUser = new LoginUser(1L, "joel@joel.com", 25);
        assertThat(loginUser.getId()).isEqualTo(1L);

        final User anonymousUser = new AnonymousUser();
        assertThatThrownBy(() -> anonymousUser.getId())
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("EMAIL 반환 기능에 대해 LoginUser는 EMAIL를, AnonymousUser는 예외를 반환한다.")
    void getEmail() {
        User loginUser = new LoginUser(1L, "joel@joel.com", 25);
        assertThat(loginUser.getEmail()).isEqualTo("joel@joel.com");

        final User anonymousUser = new AnonymousUser();
        assertThatThrownBy(() -> anonymousUser.getEmail())
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("AGE 반환 기능에 대해 LoginUser는 AGE를, AnonymousUser는 예외를 반환한다.")
    void getAge() {
        User loginUser = new LoginUser(1L, "joel@joel.com", 25);
        assertThat(loginUser.getAge()).isEqualTo(25);

        final User anonymousUser = new AnonymousUser();
        assertThatThrownBy(() -> anonymousUser.getAge())
                .isInstanceOf(AuthorizationException.class);
    }
}
