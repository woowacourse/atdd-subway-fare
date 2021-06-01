package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceFareCalculatorTest {

    @DisplayName("거리에 따른 요금 반환 확인")
    @ParameterizedTest
    @MethodSource("distance")
    void distanceFareCalculator(int distance, int expectedFare) {
        assertThat(DistanceFareCalculator.from(distance)).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> distance() {
        return Stream.of(
                Arguments.of(1, 1250),
                Arguments.of(12, 1350),
                Arguments.of(55, 2150)
        );
    }
}
