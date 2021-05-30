package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.path.domain.strategy.DistanceFarePolicy;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 계산 도메인")
class FareTest {
    private static final int DEFAULT_FARE = 1250;

    private static Stream<Arguments> calculateByDistance() {
        return Stream.of(
                Arguments.of(9 , 0, DEFAULT_FARE),
                Arguments.of(11 , 0, DEFAULT_FARE + 100),
                Arguments.of(51 , 0, DEFAULT_FARE + 900),
                Arguments.of(61 , 0, DEFAULT_FARE + 1000)
        );
    }

    @DisplayName("거리에 따른 요금이 계산된다.")
    @ParameterizedTest
    @MethodSource
    void calculateByDistance(int distance, int extraFare, int result) {
        //given
        DistanceFarePolicy distanceFarePolicy = DistanceFareFactory.create(distance, DEFAULT_FARE);
        //when
        Fare fare = new Fare(distanceFarePolicy, extraFare);
        int calculateFare = fare.calculateFare();

        //then
        assertThat(calculateFare).isEqualTo(result);
    }
}
