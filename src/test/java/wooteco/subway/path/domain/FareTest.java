package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareTest {

    @ParameterizedTest
    @DisplayName("노선별 추가 요금 계산")
    @MethodSource("extraFareByLine")
    void distanceRate(int distance, int expectedFare, int age, int extraFare) {
        //when
        Fare fare = new Fare(distance, age, extraFare);

        //then
        assertThat(fare.value()).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> extraFareByLine() {
        return Stream.of(
            Arguments.of(9, 1350, 20, 100),
            Arguments.of(9, 1450, 20, 200),
            Arguments.of(9, 1550, 20, 300)
        );
    }
}