package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.path.application.FareService;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FareServiceTest {
    private final FareService fareService = new FareService();

    private static Stream<Arguments> providedFare() {
        return Stream.of(
                Arguments.of(7, 0, 1250),
                Arguments.of(17, 0, 1450),
                Arguments.of(77, 0, 2450),
                Arguments.of(7, 1000, 2250)
        );
    }

    @ParameterizedTest(name = "[{index}] : 거리 {0}, 노선 추가 요금 {1}일 경우 요금 {2}")
    @MethodSource("providedFare")
    public void calculateTest(int distance, int extraFare, int expectedFare) {
        assertFare(distance, extraFare, 30, expectedFare);
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

    @DisplayName("거리가 7이 주어지고, 15세 나이 일 때, 청소년 요금 적용 반환")
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
