package wooteco.subway.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.path.domain.fare.age.BabyStrategy;
import wooteco.subway.path.domain.fare.age.ChildStrategy;
import wooteco.subway.path.domain.fare.age.DefaultStrategy;
import wooteco.subway.path.domain.fare.age.TeenagerStrategy;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineFixture.이호선;

@DisplayName("요금 도메인 테스트")
public class FareTest {

    @DisplayName("요금 계산 - ~ 10km")
    @Test
    void calculateFare() {
        // given
        Fare fare = new Fare(new DefaultStrategy());

        // when
        int totalFare = fare.calculate(이호선.getExtraFare(), 10);

        // then
        assertThat(totalFare).isEqualTo(1250 + 1500);
    }

    @DisplayName("요금 계산 - 10km ~ 50km")
    @Test
    void calculateFare2() {
        // given
        Fare fare = new Fare(new DefaultStrategy());

        // when
        int totalFare = fare.calculate(이호선.getExtraFare(), 47);

        // then
        assertThat(totalFare).isEqualTo(2050 + 1500);
    }

    @DisplayName("요금 계산 - 50km ~")
    @Test
    void calculateFare3() {
        // given
        Fare fare = new Fare(new DefaultStrategy());

        // when
        int totalFare = fare.calculate(이호선.getExtraFare(), 57);

        // then
        assertThat(totalFare).isEqualTo(2150 + 1500);
    }

    @DisplayName("요금 계산 - ~ 6세")
    @Test
    void calculateFareAge() {
        // given
        Fare fare = new Fare(new BabyStrategy());

        // when
        int totalFare = fare.calculate(이호선.getExtraFare(), 10);

        // then
        assertThat(totalFare).isEqualTo(0);
    }

    @DisplayName("요금 계산 - 6세 ~ 13세")
    @Test
    void calculateFareAge2() {
        // given
        Fare fare = new Fare(new ChildStrategy());

        // when
        int totalFare = fare.calculate(이호선.getExtraFare(), 10);

        // then
        assertThat(totalFare).isEqualTo(1200);
    }

    @DisplayName("요금 계산 - 13세 ~ 19세")
    @Test
    void calculateFareAge3() {
        // given
        Fare fare = new Fare(new TeenagerStrategy());

        // when
        int totalFare = fare.calculate(이호선.getExtraFare(), 10);

        // then
        assertThat(totalFare).isEqualTo(1920);
    }

    @DisplayName("요금 계산 - 19세 ~ & 비로그인 유저")
    @Test
    void calculateFareAge4() {
        // given
        Fare fare = new Fare(new DefaultStrategy());

        // when
        int totalFare = fare.calculate(이호선.getExtraFare(), 10);

        // then
        assertThat(totalFare).isEqualTo(1250 + 1500);
    }
}