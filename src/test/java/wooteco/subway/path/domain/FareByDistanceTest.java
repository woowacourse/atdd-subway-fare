package wooteco.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FareByDistanceTest {

    @ParameterizedTest(name = "거리에 따른 요금을 계산한다. {0}km -> {1}원")
    @MethodSource("calculateSource")
    void calculate(int distance, int expectedFare) {
        // given
        FareByDistance fareByDistance = new FareByDistance(distance);

        // when
        int fare = fareByDistance.calculate();

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> calculateSource() {
        return Stream.of(
                Arguments.of(10, 1250),
                Arguments.of(11, 1350),
                Arguments.of(15, 1350),
                Arguments.of(16, 1450),
                Arguments.of(50, 2050),
                Arguments.of(51, 2150),
                Arguments.of(58, 2150),
                Arguments.of(59, 2250)
        );
    }
}
