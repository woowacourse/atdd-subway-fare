package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.path.application.FareService;

import static org.assertj.core.api.Assertions.assertThat;

class FareServiceTest {
    private final FareService fareService = new FareService();

    @DisplayName("거리가 7일 때 기본 요금 반환")
    @Test
    public void calculateTest1() {
        int distance = 7;
        int expectedFare = 1250;
        assertFare(distance, 0, 30, expectedFare);
    }

    @DisplayName("거리가 17일 때 구간1 반환")
    @Test
    public void calculateTest2() {
        int distance = 17;
        int expectedFare = 1450;
        assertFare(distance, 0, 30, expectedFare);
    }

    @DisplayName("거리가 77일 때 기본 요금 반환")
    @Test
    public void calculateTest3() {
        int distance = 77;
        int expectedFare = 2450;
        assertFare(distance, 0, 30, expectedFare);
    }

    @DisplayName("추가 요금 구간 적용")
    @Test
    public void calculateTest4() {
        int distance = 7;
        int expectedFare = 1250 + 1000;
        assertFare(distance, 1000, 30, expectedFare);
    }

    @DisplayName("거리가 7주어지고, 3세의 나이일 때, 아이 요금 적용 반환")
    @Test
    public void calculateTestBaby() {
        int distance = 7;
        int expectedFare = 0;
        assertFare(distance, 0, 3, expectedFare);
    }

    @DisplayName("거리가 7이 주어지고, 9세의 나이일 때, 어린이 요금 적용 반환")
    @Test
    public void calculateTestChild() {
        int distance = 7;
        int expectedFare = (int) ((1250 - 350) * 0.5);
        assertFare(distance, 0, 9, expectedFare);
    }

    @DisplayName("거리가 7이 주어지고, 15세 나이 일 때,  청소년 요금 적용 반환")
    @Test
    public void calculateTestTeenage() {
        int distance = 7;
        int expectedFare = (int) ((1250 - 350) * 0.8);
        assertFare(distance, 0, 15, expectedFare);
    }

    private void assertFare(int distance, int extraFare, int age, int expectedFare) {
        assertThat(fareService.loginUserFare(distance, extraFare, age)).isEqualTo(expectedFare);
    }
}
