package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.path.application.FareService;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.section.domain.Distance;

public class FareServiceTest {

    @DisplayName("요금 계산 정책에 맞게 요금 계산이 진행되는지 확인")
    @ParameterizedTest
    @MethodSource("inputDistanceAndExpectFare")
    void calculateFare(int distance, int expectedFare) {
        // given
        FareService fareService = new FareService();

        // then
        assertThat(fareService.calculateFare(new Distance(distance))).isEqualTo(new Fare(expectedFare));
    }

    private static Stream<Arguments> inputDistanceAndExpectFare() {
        int defaultFare = 1250;

        return Stream.of(
            arguments(2, defaultFare),
            arguments(9, defaultFare),
            arguments(10, defaultFare),
            arguments(11, defaultFare + 100),
            arguments(15, defaultFare + 100),
            arguments(20, defaultFare + 200),
            arguments(25, defaultFare + 300),
            arguments(50, defaultFare + 800),
            arguments(51, defaultFare + 900),
            arguments(58, defaultFare + 900),
            arguments(59, defaultFare + 1_000),
            arguments(67, defaultFare + 1_100)
        );
    }
}
