package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareTest {

    @ParameterizedTest
    @DisplayName("기본 요금 계산")
    @MethodSource("matchedFare")
    void defaultRate(int distance, int predictedFare) {
        //when
        Fare fare = new Fare(distance);

        //then
        assertThat(fare.value()).isEqualTo(predictedFare);
    }

    private static Stream<Arguments> matchedFare() {
        return Stream.of(
            Arguments.of(9, 1250),
            Arguments.of(12, 1350),
            Arguments.of(66, 2250)
        );
    }
}