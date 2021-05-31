package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceTest {
    @DisplayName("10km 초과 ∼ 50km 까지면 true, 아니면 false")
    @ParameterizedTest
    @CsvSource({"10,false", "11,true", "50,true", "51,false"})
    void isFirstOverCharge(int distanceValue, boolean result) {
        Distance distance = new Distance(distanceValue);
        assertThat(distance.isFirstOverCharge()).isEqualTo(result);
    }

    @DisplayName("50km 초과면 true, 아니면 false")
    @ParameterizedTest
    @CsvSource({"50,false", "51,true"})
    void isSecondOverCharge(int distanceValue, boolean result) {
        Distance distance = new Distance(distanceValue);
        assertThat(distance.isSecondOverCharge()).isEqualTo(result);
    }

    @DisplayName("10km 이하면 true, 아니면 false")
    @ParameterizedTest
    @CsvSource({"9,true", "10,true", "11,false"})
    void isNoOverCharge(int distanceValue, boolean result) {
        Distance distance = new Distance(distanceValue);
        assertThat(distance.isNoOverCharge()).isEqualTo(result);
    }

    @DisplayName("10km 초과 ∼ 50km 범위내에서 5km 마다 100원 추가")
    @Test
    void calculateFirstOverCharge() {
        Distance distance = new Distance(35);
        assertThat(distance.calculateFirstOverCharge()).isEqualTo(500);

        Distance distance2 = new Distance(36);
        assertThat(distance2.calculateFirstOverCharge()).isEqualTo(600);
    }

    @DisplayName("50km 초과 범위내에서 8km 마다 100원 추가")
    @Test
    void calculateSecondOverCharge() {
        Distance distance = new Distance(58);
        assertThat(distance.calculateSecondOverCharge()).isEqualTo(800 + 100);

        Distance distance2 = new Distance(59);
        assertThat(distance2.calculateSecondOverCharge()).isEqualTo(800 + 200);
    }

    @DisplayName("비교하고자 하는 거리보다 짧거나 같으면 true, 아니면 false")
    @ParameterizedTest
    @CsvSource({"1,false", "2,true", "3,true"})
    void isShorterOrEqualTo(int distance, boolean result) {
        Distance thisDistance = new Distance(2);
        Distance thatDistance = new Distance(distance);

        assertThat(thisDistance.isShorterOrEqualTo(thatDistance)).isEqualTo(result);
    }
}