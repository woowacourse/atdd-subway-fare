package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("연령별 요금을 계산한다.")
    @Test
    void fareCalculateWithAge() {
        LoginMember 청소년 = new LoginMember(1L, "yjksw@github.com", 13);
        LoginMember 어린이 = new LoginMember(2L, "fjzjqhdl@gmail.com", 6);
        LoginMember 일반인 = new LoginMember(3L, "nabom@gmail.com", 30);

        GuestMember 게스트 = new GuestMember();
        int baseFare = 1250;

        assertThat(청소년.calculateFare(baseFare)).isEqualTo(720); // 350원 공제 후 80%
        assertThat(어린이.calculateFare(baseFare)).isEqualTo(450); // 350원 공제 후 50%
        assertThat(일반인.calculateFare(baseFare)).isEqualTo(1250); // 그대로
        assertThat(게스트.calculateFare(baseFare)).isEqualTo(1250); // 그대로
    }

}