package wooteco.subway.path.domain.fare.fareStrategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DistanceAppliedRuleTest {

    @ParameterizedTest
    @DisplayName("거리별 요금 계산")
    @MethodSource("matchedDistance")
    void create(int distance, int fare) {
        assertThat(DistanceAppliedRule.calculateDistanceFare(distance))
            .isEqualTo(fare);
    }

    private static Stream<Arguments> matchedDistance() {
        return Stream.of(
            Arguments.of(9, 1250),
            Arguments.of(16, 1450),
            Arguments.of(66, 2250)
        );
    }
}