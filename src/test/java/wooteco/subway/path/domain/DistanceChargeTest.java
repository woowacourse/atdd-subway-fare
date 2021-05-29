package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DistanceChargeTest {

    @DisplayName("거리별 요금 정책에 따라 해당하는 추가 요금을 부과한다.")
    @Test
    void calculateDistanceCharge() {
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(11);
        Distance distance3 = new Distance(51);

        assertThat(DistanceCharge.calculateDistanceCharge(distance1)).isEqualTo(0);
        assertThat(DistanceCharge.calculateDistanceCharge(distance2)).isEqualTo(100);
        assertThat(DistanceCharge.calculateDistanceCharge(distance3)).isEqualTo(800 + 100);
    }
}