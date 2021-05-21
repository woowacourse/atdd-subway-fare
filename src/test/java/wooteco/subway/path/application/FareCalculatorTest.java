package wooteco.subway.path.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.path.domain.Fare;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {
    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        this.fareCalculator = new FareCalculator();
    }

    @ParameterizedTest
    @CsvSource({
            "1250, 9, 1250",
            "1250, 12, 1350",
            "1250, 16, 1450",
            "1250, 58, 2150",
            "1250, 66, 2250"
    })
    @DisplayName("거리에 따라 부가되는 추가요금을 계산한다.")
    void getFareByDistance(int fareRaw, int distance, int fareExpected) {
        Fare fare = new Fare(fareRaw);

        Fare actual = fareCalculator.getExtraFareByDistance(fare, distance);

        assertThat(actual).isEqualTo(new Fare(fareExpected));
    }
}