package wooteco.subway.path.domain.fare.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.path.domain.fare.Fare;

class DistanceFareTest {
    @DisplayName("거리에 따라 부가되는 추가요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({
        "9, 1250",
        "12, 1350",
        "16, 1450",
        "58, 2150",
        "66, 2250"
    })
    void getFareByDistance(int distance, int fareExpected) {
        // given
        Fare currentFare = new Fare(1_250);

        // when
        DistanceFare distanceFare = DistanceFare.of(distance);
        Fare fareWithDistanceExtraFare = distanceFare.getFare(currentFare);

        // then
        assertThat(fareWithDistanceExtraFare).isEqualTo(new Fare(fareExpected));
    }
}