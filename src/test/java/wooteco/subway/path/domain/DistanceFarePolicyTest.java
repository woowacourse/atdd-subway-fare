package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {
    @DisplayName("거리별 요금 계산")
    @ParameterizedTest
    @CsvSource({"10,1250", "11, 1350", "50, 2050", "100, 2750"})
    void distancePolicyTest(int distance, int expectedFare) {
        //given
        //when
        Fare fare = DistanceFarePolicy.calculateFare(distance);
        //then
        assertThat(fare).isEqualTo(new Fare(expectedFare));
    }
}