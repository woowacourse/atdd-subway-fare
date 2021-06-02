package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NonLoginMemberTest {

    private static final int DEFAULT_FARE = 1250;

    @DisplayName("로그인 여부 확인")
    @Test
    void isLoggedIn() {
        // given
        AuthMember nonLoginMember = new NonLoginMember();

        // when
        boolean loggedIn = nonLoginMember.isLoggedIn();

        // then
        assertThat(loggedIn).isFalse();
    }


    @DisplayName("요금이 할인되지 않는다.")
    @Test
    void getDiscountedFare() {
        // given
        AuthMember nonLoginMember = new NonLoginMember();

        // when
        int discountedFare = nonLoginMember.getDiscountedFare(DEFAULT_FARE);

        // then
        assertThat(discountedFare).isEqualTo(DEFAULT_FARE);
    }
}