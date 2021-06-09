package wooteco.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.exception.application.ValidationFailureException;

class FareTest {

    @DisplayName("동등성 검사")
    @Test
    void equality() {
        // when, then
        Fare fare = new Fare(10);
        assertThat(fare).isEqualTo(new Fare(10));
    }

    @DisplayName("요금이 음수이면 예외 발생")
    @Test
    void create_fail_negativeFare() {
        assertThatThrownBy(() -> new Fare(-1))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("요금은 음수일 수 없습니다");
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

    @DisplayName("요금 빼기")
    @Test
    void minusFare() {
        // given
        Fare fare = new Fare(1000);

        // when, then
        assertThat(fare.minus(500))
            .isEqualTo(new Fare(500));
    }

    @DisplayName("요금에 스케일 곱하기")
    @MethodSource("multiplyScaleSource")
    @ParameterizedTest
    void multiplyScale(double scale, Fare expectedFare) {
        // given
        Fare fare = new Fare(1000);

        // when, then
        assertThat(fare.multiplyScale(scale))
            .isEqualTo(expectedFare);
    }

    private static Stream<Arguments> multiplyScaleSource() {
        return Stream.of(
            Arguments.of(0.0, new Fare(0)),
            Arguments.of(0.3, new Fare(300)),
            Arguments.of(0.9, new Fare(900)),
            Arguments.of(2.0, new Fare(2000)),
            Arguments.of(2.8, new Fare(2800))
        );
    }
}
