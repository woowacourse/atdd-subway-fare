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

    @DisplayName("거리에 따라 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {
        "5:0:20:1250", "10:0:20:1250", "11:0:20:1350", "24:0:20:1550", "50:0:20:2050",
        "51:0:20:2150", "55:0:20:2150", "56:0:20:2150", "58:0:20:2150", "59:0:20:2250"
    }, delimiter = ':')
    void testOfSubwayFareByDistance(int distance, int extraFare, int age, int expected) {
        // when
        Fare maxExtraFare = new Fare(extraFare);
        Fare actual = Fare.ofSubwayFare(distance, maxExtraFare, age);

        // then
        assertThat(actual).isEqualTo(new Fare(expected));
    }

    @DisplayName("나이에 따라 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {
        "51:0:5:0", "51:0:6:1250", "51:0:12:1250", "51:0:13:1790", "51:0:19:1790", "51:0:20:2150"
    }, delimiter = ':')
    void testOfSubwayFareByAge(int distance, int extraFare, int age, int expected) {
        // when
        Fare maxExtraFare = new Fare(extraFare);
        Fare actual = Fare.ofSubwayFare(distance, maxExtraFare, age);

        // then
        assertThat(actual).isEqualTo(new Fare(expected));
    }

    @DisplayName("노선 추가 요금에 따라 요금을 계산한다")
    @ParameterizedTest
    @CsvSource(value = {
        "51:0:20:2150", "51:100:20:2250", "51:200:20:2350", "51:300:20:2450", "51:500:20:2650"
    }, delimiter = ':')
    void testOfSubwayFareByMaxExtraFare(int distance, int extraFare, int age, int expected) {
        // when
        Fare maxExtraFare = new Fare(extraFare);
        Fare actual = Fare.ofSubwayFare(distance, maxExtraFare, age);

        // then
        assertThat(actual).isEqualTo(new Fare(expected));
    }

}
