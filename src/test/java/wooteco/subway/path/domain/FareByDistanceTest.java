package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareByDistanceTest {

    @Test
    @DisplayName("10Km 이하는 1250원이다")
    void calculateBelow10() {
        final int fare = FareByDistance.calculate(9);
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("10Km 초과은 50Km 이하은 기존요금에 5KM당 100원 추가이다")
    void calculateUp10Below50() {
        int fare = FareByDistance.calculate(15);
        assertThat(fare).isEqualTo(1350);

        fare = FareByDistance.calculate(25);
        assertThat(fare).isEqualTo(1550);

        fare = FareByDistance.calculate(30);
        assertThat(fare).isEqualTo(1650);

        fare = FareByDistance.calculate(45);
        assertThat(fare).isEqualTo(1950);

        fare = FareByDistance.calculate(50);
        assertThat(fare).isEqualTo(2050);
    }

    @Test
    @DisplayName("50Km 초과는 기존 요금 정책에 8KM 당 100원 추가이다")
    void calculateUp50() {
        int fare = FareByDistance.calculate(58);
        assertThat(fare).isEqualTo(2150);

        fare = FareByDistance.calculate(66);
        assertThat(fare).isEqualTo(2250);

        fare = FareByDistance.calculate(74);
        assertThat(fare).isEqualTo(2350);

        fare = FareByDistance.calculate(82);
        assertThat(fare).isEqualTo(2450);
    }
}