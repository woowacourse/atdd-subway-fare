package wooteco.subway.path.domain.fare.creationstrategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.path.domain.fare.Fare;

class DistanceBasedCreationStrategyTest {

    @DisplayName("거리별 기본 요금 계산")
    @MethodSource("distanceFareValues")
    @ParameterizedTest
    void defaultRate(int distance, Fare expectedFare) {
        // given
        DistanceBasedCreationStrategy distanceBasedCreationStrategy = new DistanceBasedCreationStrategy();

        //when
        Fare fare = distanceBasedCreationStrategy.generate(distance);

        //then
        assertThat(fare.value()).isEqualTo(expectedFare.value());
    }

    private static Stream<Arguments> distanceFareValues() {
        return Stream.of(
            Arguments.of(0, new Fare(1250)),
            Arguments.of(10, new Fare(1250)),
            Arguments.of(11, new Fare(1350)),
            Arguments.of(16, new Fare(1450)),
            Arguments.of(25, new Fare(1550)),
            Arguments.of(45, new Fare(1950)),
            Arguments.of(58, new Fare(2150)),
            Arguments.of(66, new Fare(2250)),
            Arguments.of(154, new Fare(3350))
        );
    }
}
