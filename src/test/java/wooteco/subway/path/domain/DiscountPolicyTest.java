package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class DiscountPolicyTest {

    @DisplayName("19살 이상은 성인 정책이다.")
    @Test
    void findByAdultAge() {
        //when
        DiscountPolicy adult = DiscountPolicy.findByAge(19);

        //then
        assertThat(adult).isEqualTo(DiscountPolicy.ADULT);
    }

    @DisplayName("13살 이상 19살 미만은 청소년 정책이다.")
    @Test
    void findByTeenagerAge() {
        //when
        DiscountPolicy teenager1 = DiscountPolicy.findByAge(18);
        DiscountPolicy teenager2 = DiscountPolicy.findByAge(13);

        //then
        assertThat(teenager1).isEqualTo(DiscountPolicy.TEENAGER);
        assertThat(teenager2).isEqualTo(DiscountPolicy.TEENAGER);
    }

    @DisplayName("6살 이상 13살 미만은 어린이 정책이다.")
    @Test
    void findByChildAge() {
        //when
        DiscountPolicy teenager1 = DiscountPolicy.findByAge(12);
        DiscountPolicy teenager2 = DiscountPolicy.findByAge(6);

        //then
        assertThat(teenager1).isEqualTo(DiscountPolicy.CHILD);
        assertThat(teenager2).isEqualTo(DiscountPolicy.CHILD);
    }

    @DisplayName("6살 미만은 청소년 정책이다.")
    @Test
    void findByBabyAge() {
        //when
        DiscountPolicy teenager1 = DiscountPolicy.findByAge(5);
        DiscountPolicy teenager2 = DiscountPolicy.findByAge(1);

        //then
        assertThat(teenager1).isEqualTo(DiscountPolicy.BABY);
        assertThat(teenager2).isEqualTo(DiscountPolicy.BABY);
    }

    @DisplayName("0살 이하로 정책을 찾으려면 예외가 발생한다.")
    @Test
    void findByNegativeAge() {
        //when then
        assertThatThrownBy(() -> DiscountPolicy.findByAge(-1))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> DiscountPolicy.findByAge(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}