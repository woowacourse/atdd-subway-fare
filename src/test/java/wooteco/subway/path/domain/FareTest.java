package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @DisplayName("요금 객체를 생성한다")
    @Test
    void testInitPrice() {
        // given
        int value = 10_000;

        // when // then
        assertThatCode(() -> new Fare(value))
                .doesNotThrowAnyException();
    }

    @DisplayName("가격이 음수이면 예외를 발생시킨다")
    @Test
    void testInitPriceIfValueIsMinus() {
        // given
        int minusValue = -1_000;

        // when // then
        assertThatThrownBy(() -> new Fare(minusValue))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("자주 사용하는 요금 객체를 캐싱한다")
    @Test
    void testCachedPrice() {
        // given
        int basicValue = 1_250;
        int additionValue = 100;

        // when
        Fare basicFare = Fare.BASIC;
        Fare additionFare = Fare.ADDITION;

        // then
        assertThat(basicFare).isEqualTo(new Fare(basicValue));
        assertThat(additionFare).isEqualTo(new Fare(additionValue));
    }

    @DisplayName("가격을 더한다")
    @Test
    void testAdd() {
        // given
        Fare first = new Fare(1_000);
        Fare second = new Fare(500);

        // when
        Fare result = first.add(second);

        // then
        assertThat(result).isEqualTo(new Fare(1_500));
    }

    @DisplayName("거리에 따른 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {
            "5:1250", "10:1250", "11:1350", "24:1550", "50:2050", "51:2150", "55:2150", "56:2150", "58:2150",
            "59:2250"
    }, delimiter = ':')
    void testCalculateRate(int distance, int expected) {
        // when
        Fare actual = Fare.calculateRate(distance, 0);

        // then
        assertThat(actual).isEqualTo(new Fare(expected));
    }
}