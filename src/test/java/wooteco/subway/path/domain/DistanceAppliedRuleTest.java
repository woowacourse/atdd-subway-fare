package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DistanceAppliedRuleTest {

    private static final int BASIC_FARE = 1250;

    @ParameterizedTest
    @DisplayName("거리별 기본 요금 계산")
    @MethodSource("distanceFare")
    void defaultRate(int distance, int expectedFare) {
        //when
        int fare = DistanceAppliedRule.applyRule(BASIC_FARE, distance);

        //then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> distanceFare() {
        return Stream.of(
            Arguments.of(9, 1250),
            Arguments.of(12, 1350),
            Arguments.of(66, 2250)
        );
    }
}