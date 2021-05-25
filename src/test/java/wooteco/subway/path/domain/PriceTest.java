package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @DisplayName("요금 객체를 생성한다")
    @Test
    void testInitPrice() {
        // given
        BigDecimal value = BigDecimal.valueOf(10_000);

        // when // then
        assertThatCode(() -> new Price(value))
                .doesNotThrowAnyException();
    }

    @DisplayName("가격이 음수이면 예외를 발생시킨다")
    @Test
    void testInitPriceIfValueIsMinus() {
        // given
        BigDecimal minusValue = BigDecimal.valueOf(-1_000);

        // when // then
        assertThatThrownBy(() -> new Price(minusValue))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("자주 사용하는 요금 객체를 캐싱한다")
    @Test
    void testCachedPrice() {
        // given
        BigDecimal basicValue = BigDecimal.valueOf(1_250);
        BigDecimal additionValue = BigDecimal.valueOf(100);

        // when
        Price basicPrice = Price.BASIC;
        Price additionPrice = Price.ADDITION;

        // then
        assertThat(basicPrice).isEqualTo(new Price(basicValue));
        assertThat(additionPrice).isEqualTo(new Price(additionValue));
    }

    @DisplayName("가격을 더한다")
    @Test
    void testAdd() {
        // given
        Price first = new Price(1_000);
        Price second = new Price(500);

        // when
        Price result = first.add(second);

        // then
        assertThat(result).isEqualTo(new Price(1_500));
    }

    @DisplayName("거리에 따른 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {
            "5:1250", "10:1250", "11:1350", "24:1550", "50:2050", "51:2150", "55:2150", "56:2150", "58:2150",
            "59:2250"
    }, delimiter = ':')
    void testCalculateRate(int distance, BigDecimal expected) {
        // when
        Price actual = Price.calculateRate(distance);

        // then
        assertThat(actual).isEqualTo(new Price(expected));
    }
}