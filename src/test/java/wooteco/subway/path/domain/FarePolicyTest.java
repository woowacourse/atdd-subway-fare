package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyTest {

    @DisplayName("거리가 7일 때 기본 요금 반환")
    @Test
    public void calculateTest1() {
        int distance = 7;
        int expectedFare = 1250;
        assertFare(distance, expectedFare);
    }

    @DisplayName("거리가 17일 때 구간1 반환")
    @Test
    public void calculateTest2() {
        int distance = 17;
        int expectedFare = 1450;
        assertFare(distance, expectedFare);
    }

    @DisplayName("거리가 77일 때 기본 요금 반환")
    @Test
    public void calculateTest3() {
        int distance = 77;
        int expectedFare = 2450;
        assertFare(distance, expectedFare);
    }

    private void assertFare(int distance, int expectedFare) {
        assertThat(FarePolicy.calculate(distance)).isEqualTo(expectedFare);
    }
}