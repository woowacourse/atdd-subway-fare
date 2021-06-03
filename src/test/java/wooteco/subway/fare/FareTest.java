package wooteco.subway.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.path.domain.fare.Fare;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @Test
    @DisplayName("거리가 10키로 이하인 경우 기본요금 계산")
    void testCalculateFareWhenDistanceIsUnder10() {
        int distance = 10;
        int extraFare = 0;
        Fare fare = Fare.of(distance, extraFare);

        assertThat(fare.toInt()).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 10키로 초과, 50키로 이하인 경우 5km마다 100원이 추가된 요금을 반환한다.")
    void testCalculateFareWhenDistanceIsBetween10To50() {
        int distance = 30; // 10 + 20
        int distance2 = 40; // 10 + 30
        int distance3 = 50; // 10 + 40

        int extraFare = 0;
        Fare fare = Fare.of(distance, extraFare);
        Fare fare2 = Fare.of(distance2, extraFare);
        Fare fare3 = Fare.of(distance3, extraFare);

        assertThat(fare.toInt()).isEqualTo(1250 + 400);
        assertThat(fare2.toInt()).isEqualTo(1250 + 600);
        assertThat(fare3.toInt()).isEqualTo(1250 + 800);
    }

    @Test
    @DisplayName("거리가 50키로를 초과하는 경우 10-40 구간 100원/5km, 50 초과 구간 100원/8km 의 추가요금이 반환된다.")
    void testCalculateFareWhenDistanceIsOver50() {
        int distance = 58; // 10 + 40 + 8

        int extraFare = 0;
        Fare fare = Fare.of(distance, extraFare);

        assertThat(fare.toInt()).isEqualTo(1250 + 800 + 100);
    }

    @Test
    @DisplayName("추가요금이 있고, 거리가 10키로 이하인 경우 기본요금에 추가요금을 더한 값을 반환한다.")
    void testCalculateFareWhenDistanceIsUnder10AndExistExtraFare() {
        int distance = 10;
        int extraFare = 400;
        Fare fare = Fare.of(distance, extraFare);

        assertThat(fare.toInt()).isEqualTo(1250 + extraFare);
    }

    @Test
    @DisplayName("거리가 10키로 초과, 50키로 이하인 경우 10km이상 구간부터 5km마다 100원이 추가된 요금을 반환한다.")
    void testCalculateFareWhenDistanceIsBetween10To50AndExistExtraFare() {
        int distance = 30; // 10 + 20
        int distance2 = 40; // 10 + 30
        int distance3 = 50; // 10 + 40

        int extraFare = 900;
        Fare fare = Fare.of(distance, extraFare);
        Fare fare2 = Fare.of(distance2, extraFare);
        Fare fare3 = Fare.of(distance3, extraFare);

        assertThat(fare.toInt()).isEqualTo(1250 + 400 + extraFare);
        assertThat(fare2.toInt()).isEqualTo(1250 + 600 + extraFare);
        assertThat(fare3.toInt()).isEqualTo(1250 + 800 + extraFare);
    }

    @Test
    @DisplayName("거리가 50키로를 초과하는 경우 10-40 구간 100원/5km, 50 초과 구간 100원/8km 의 추가요금이 반환된다.")
    void testCalculateFareWhenDistanceIsOver50AndExistExtraFare() {
        int distance = 58; // 10 + 40 + 8

        int extraFare = 1200;
        Fare fare = Fare.of(distance, extraFare);

        assertThat(fare.toInt()).isEqualTo(1250 + 800 + 100 + extraFare);
    }
}
