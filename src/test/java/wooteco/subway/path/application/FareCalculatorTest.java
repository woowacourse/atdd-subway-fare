package wooteco.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Fare;

class FareCalculatorTest {
    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        this.fareCalculator = new FareCalculator();
    }

    @DisplayName("거리에 따라 부가되는 추가요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({
        "1250, 9, 1250",
        "1250, 12, 1350",
        "1250, 16, 1450",
        "1250, 58, 2150",
        "1250, 66, 2250"
    })
    void getFareByDistance(int fareRaw, int distance, int fareExpected) {
        Fare fare = new Fare(fareRaw);

        Fare actual = fareCalculator.getExtraFareByDistance(fare, distance);

        assertThat(actual).isEqualTo(new Fare(fareExpected));
    }

    @DisplayName("경로상 노선들의 추가 요금에 따른 총 요금을 계산한다.")
    @ParameterizedTest
    @MethodSource
    void getFareWithLineExtraFare(Fare currentFare, Fare expectedFare, List<Line> lines) {
        Fare actualFare = fareCalculator.getFareWithLineExtraFare(currentFare, lines);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    static Stream<Arguments> getFareWithLineExtraFare() {
        return Stream.of(
            Arguments.of(new Fare(1250), new Fare(2150),
                Collections.singletonList(new Line("1호선", "1번색깔", new Fare(900)))),
            Arguments.of(new Fare(1350), new Fare(2250),
                Collections.singletonList(new Line("1호선", "1번색깔", new Fare(900)))),
            Arguments.of(new Fare(1250), new Fare(2150), Arrays.asList(
                new Line("1호선", "1번색깔", new Fare(0)),
                new Line("2호선", "2번색깔", new Fare(500)),
                new Line("3호선", "3번색깔", new Fare(900))
                )
            )
        );
    }

    @DisplayName("나이에 따른 요금 할인 정책을 적용한다.")
    @ParameterizedTest(name="{3}")
    @CsvSource({
            "18, 1250, 720, 청소년",
            "12, 1250, 450, 어린이",
            "19, 1250, 1250, 성인"
    })
    void getFareWithAge(int age, int currentFare, int expectedFare, String testCaseName) {
        Fare actualFare = fareCalculator.getFareByAge(age, new Fare(currentFare));
        assertThat(actualFare).isEqualTo(new Fare(expectedFare));
    }
}