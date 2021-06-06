package wooteco.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @DisplayName("동등성 검사")
    @Test
    void equality() {
        // when, then
        Fare fare = new Fare(10);
        assertThat(fare).isEqualTo(new Fare(10));
    }

    @DisplayName("요금과 요금 더하기")
    @Test
    void addFare() {
        // given
        Fare fare = new Fare(500);

        // when, then
        assertThat(fare.addFare(new Fare(1000)))
            .isEqualTo(new Fare(1500));
    }
}