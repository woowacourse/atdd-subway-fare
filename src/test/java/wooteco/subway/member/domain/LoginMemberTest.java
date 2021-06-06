package wooteco.subway.member.domain;

import org.junit.jupiter.api.Test;
import sun.rmi.runtime.Log;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LoginMemberTest {
    private static final String EMAIL = "email@email.com";
    private LoginMember loginMember = new LoginMember(1L, EMAIL, 10);

    @Test
    void empty() {
        LoginMember empty = LoginMember.empty();
        assertThat(empty.getId()).isNull();
    }

    @Test
    void isPresent() {
        LoginMember empty = LoginMember.empty();
        assertThat(empty.isPresent()).isFalse();
    }

    @Test
    void getId() {
        assertThat(loginMember.getId()).isEqualTo(1L);
    }

    @Test
    void getEmail() {
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void getAge() {
        assertThat(loginMember.getAge()).isEqualTo(10);
    }
}