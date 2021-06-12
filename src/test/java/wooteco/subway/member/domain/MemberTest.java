package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.member.application.MemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;

    @Nested
    @DisplayName("Member 생성은")
    class MemberCreation {
        @Nested
        @DisplayName("정상 Email, Password, Age 가 입력된다면")
        class ContextValid {
            @Test
            @DisplayName("정상 멤버 객체가 생성된다.")
            void itReturnsValidMember() {
                assertThat(new Member(EMAIL, PASSWORD, AGE)).isNotNull();
            }
        }

        @Nested
        @DisplayName("만약 Empty 이메일이 입력된다면")
        class ContextEmptyEmail {
            @Test
            @DisplayName("MemberException 을 리턴한다.")
            void itReturnsMemberException() {
                assertThatThrownBy(() -> new Member("", PASSWORD, AGE)).isInstanceOf(MemberException.class);
            }
        }

        @Nested
        @DisplayName("만약 Empty 비밀번호가 입력된다면")
        class ContextEmptyPassword {
            @Test
            @DisplayName("MemberException 을 리턴한다.")
            void itReturnsMemberException() {
                assertThatThrownBy(() -> new Member(EMAIL, "", AGE)).isInstanceOf(MemberException.class);
            }
        }

        @Nested
        @DisplayName("만약 음수의 나이가 입력된다면")
        class ContextInValidAge {
            @Test
            @DisplayName("MemberException 을 리턴한다.")
            void itReturnsMemberException() {
                assertThatThrownBy(() -> new Member(EMAIL, PASSWORD, -1)).isInstanceOf(MemberException.class);
            }
        }
    }
}
