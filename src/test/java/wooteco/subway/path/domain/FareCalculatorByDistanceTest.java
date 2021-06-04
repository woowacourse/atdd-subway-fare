package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorByDistanceTest {

    @ParameterizedTest
    @CsvSource({"9,1250", "12,1350", "50,2050"})
    @DisplayName("거리에 따른 요금을 구한다.")
    void from(int distance, int expected) {
        int result = FareCalculatorByDistance.from(distance);

        assertThat(result).isEqualTo(expected);
    }
}