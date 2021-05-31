package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceChargeTest {
    @DisplayName("거리에 따라 요금이 부과된다. 10km 이하는 추가 요금 0, 10km 초과 ~ 50km 이하 5km 마다 100원, 50km 초과 부터는 8km 마다 100원")
    @ParameterizedTest
    @CsvSource({"10,0", "11,100", "51,900"})
    void calculateDistanceCharge(int distance, int expectedCharge) {
        Distance distance1 = new Distance(distance);
        assertThat(DistanceCharge.calculateDistanceCharge(distance1)).isEqualTo(expectedCharge);
    }
}