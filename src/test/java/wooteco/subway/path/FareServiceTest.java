package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.path.application.FareService;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.section.domain.Distance;

public class FareServiceTest {

    @Test
    @DisplayName("요금 계산 정책에 맞게 요금 계산이 진행되는지 확인")
    void calculateFare() {
        // given
        FareService fareService = new FareService();

        // when

        // then
        assertThat(fareService.calculateFare(new Distance(2))).isEqualTo(new Fare(1250));
        assertThat(fareService.calculateFare(new Distance(9))).isEqualTo(new Fare(1250));
        assertThat(fareService.calculateFare(new Distance(10))).isEqualTo(new Fare(1250));
        assertThat(fareService.calculateFare(new Distance(11))).isEqualTo(new Fare(1250 + 100));
        assertThat(fareService.calculateFare(new Distance(15))).isEqualTo(new Fare(1250 + 100));
        assertThat(fareService.calculateFare(new Distance(20))).isEqualTo(new Fare(1250 + 200));
        assertThat(fareService.calculateFare(new Distance(25))).isEqualTo(new Fare(1250 + 300));
        assertThat(fareService.calculateFare(new Distance(50))).isEqualTo(new Fare(1250 + 800));
        assertThat(fareService.calculateFare(new Distance(51))).isEqualTo(new Fare(1250 + 900));
        assertThat(fareService.calculateFare(new Distance(58))).isEqualTo(new Fare(1250 + 900));
        assertThat(fareService.calculateFare(new Distance(59))).isEqualTo(new Fare(1250 + 1000));
        assertThat(fareService.calculateFare(new Distance(67))).isEqualTo(new Fare(1250 + 1100));
    }
}
