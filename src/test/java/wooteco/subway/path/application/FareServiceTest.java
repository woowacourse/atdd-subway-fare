package wooteco.subway.path.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareServiceTest {

    FareService fareService;

    @BeforeEach
    void setUp() {
        fareService = new FareService();
    }

    @Test
    @DisplayName("거리에 따라 적절한 요금을 반환한다.")
    void calculateFare() {
        assertThat(fareService.calculateFare(10)).isEqualTo(1250);
        assertThat(fareService.calculateFare(12)).isEqualTo(1350);
        assertThat(fareService.calculateFare(16)).isEqualTo(1450);
        assertThat(fareService.calculateFare(75)).isEqualTo(2450);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선의 경우 추가요금을 포함한 요금을 반환한다.")
    void calculateExtraFare() {
        final int fare = fareService.calculateFare(10);
        List<Line> lines = Arrays.asList(new Line(1L, "마동석", "red", 100),
                new Line(2L, "마동선", "green"));
        assertThat(fareService.calculateExtraFare(fare, lines)).isEqualTo(1350);
    }

    @Test
    @DisplayName("로그인 회원의 경우 할인금액을 적용한다.")
    void discountFareByAge() {
        final int fare = fareService.calculateFare(10);
        assertThat(fareService.discountFareByAge(fare, 24)).isEqualTo(1250);
        assertThat(fareService.discountFareByAge(fare, 13)).isEqualTo(720);
        assertThat(fareService.discountFareByAge(fare, 6)).isEqualTo(450);
    }
}