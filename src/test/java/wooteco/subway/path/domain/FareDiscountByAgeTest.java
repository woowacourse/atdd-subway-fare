package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareDiscountByAgeTest {
    private static final int DEDUCTIBLE_FARE = 350;

    @Test
    @DisplayName("6세 이상, 13세 미만에게 공제 금액을 제한 50% 할인을 적용한다.")
    void calculateChildren() {
        int originalFare = 1000;
        int age = 12;

        final int finalFare = FareDiscountByAge.calculate(age, originalFare);

        final double expectedFare = (originalFare - DEDUCTIBLE_FARE) * 0.5 + DEDUCTIBLE_FARE;
        assertThat(finalFare).isEqualTo((int)expectedFare);
    }

    @Test
    @DisplayName("13세 이상, 19세 미만에게 공제 금액을 제한 20% 할인을 적용한다.")
    void calculateTeenager() {
        int originalFare = 1000;
        int age = 18;

        final int finalFare = FareDiscountByAge.calculate(age, originalFare);

        final double expectedFare = (originalFare - DEDUCTIBLE_FARE) * 0.8 + DEDUCTIBLE_FARE;
        assertThat(finalFare).isEqualTo((int)expectedFare);
    }

    @Test
    @DisplayName("19세 이상에게는 할인이 없다")
    void calculateAdult() {
        int originalFare = 1000;
        int age = 20;

        final int finalFare = FareDiscountByAge.calculate(age, originalFare);
        assertThat(finalFare).isEqualTo(originalFare);
    }
}