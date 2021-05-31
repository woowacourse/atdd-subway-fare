package wooteco.subway.member.domain;

import org.junit.jupiter.api.Test;
import wooteco.subway.exception.AuthorizationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    private final static String EMAIL = "email@email.com";
    private final static String PASSWORD = "1234";
    private final static int AGE = 10;

    private Member member = new Member(1L, EMAIL, PASSWORD, AGE);

    @Test
    void getId() {
        assertThat(member.getId()).isEqualTo(1L);
    }

    @Test
    void getEmail() {
        assertThat(member.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void getPassword() {
        assertThat(member.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void getAge() {
        assertThat(member.getAge()).isEqualTo(AGE);
    }

    @Test
    void checkPassword_inTrueCase() {
        assertDoesNotThrow(() -> member.checkPassword(PASSWORD));
    }

    @Test
    void checkPassword_inFalseCase() {
        assertThatThrownBy(() -> member.checkPassword("1"))
                .isInstanceOf(AuthorizationException.class);
    }
}