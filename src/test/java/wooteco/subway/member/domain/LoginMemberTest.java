package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.ParameterizedTest.ARGUMENTS_PLACEHOLDER;

class LoginMemberTest {

    private static final long ID = 1L;
    private static final String EMAIL = "a@a.com";
    private static final int DEFAULT_FARE = 1250;

    @DisplayName("로그인 여부 확인")
    @Test
    void isLoggedIn() {
        // given
        AuthMember loginMember = new LoginMember(ID, EMAIL, 20);

        // when
        boolean loggedIn = loginMember.isLoggedIn();

        // then
        assertThat(loggedIn).isTrue();
    }

    @ParameterizedTest(name = "나이에 따라 요금이 할인된다. " + ARGUMENTS_PLACEHOLDER)
    @MethodSource("getDiscountedFareSource")
    void getDiscountedFare(int age, int expectedFare) {
        // given
        AuthMember loginMember = new LoginMember(ID, EMAIL, age);

        // when
        int discountedFare = loginMember.getDiscountedFare(DEFAULT_FARE);

        // then
        assertThat(discountedFare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> getDiscountedFareSource() {
        return Stream.of(
                Arguments.of(6, 450),
                Arguments.of(12, 450),
                Arguments.of(13, 720),
                Arguments.of(18, 720),
                Arguments.of(19, 1250)
        );
    }
}