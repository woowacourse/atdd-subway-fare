package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

public class MemberTest {

    @DisplayName("나이는 음수가 불가")
    @Test
    void negativeAge() {
        assertThatCode(() -> new Member("a@a.com", "password", -1))
                .isInstanceOf(Exception.class);
    }

    @DisplayName("비밀번호를 비교한다.")
    @Test
    void checkPassword() {
        // given
        String password = "password";
        Member member = new Member("a@a.com", password, 20);

        // when, then
        assertThatCode(() -> member.checkPassword("invalid password"))
                .isInstanceOf(Exception.class);
        assertThatCode(() -> member.checkPassword(password))
                .doesNotThrowAnyException();
    }
}
