package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.service.AgeDiscountFareCalculator;
import wooteco.subway.service.DefaultFareCalculator;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    private FareCalculator fareCalculator = new DefaultFareCalculator();

    @DisplayName("요금 정책 - 거리에 따른 추가 금액 계산")
    @ParameterizedTest
    @CsvSource(value = {"1:1250", "10:1250", "11:1350", "16:1450", "50:2050", "58:2150", "59:2250"}, delimiter = ':')
    void defaultCalculateFare(int distance, int expectedFare) {
        //given
        int extraFare = 0;

        //when
        int actualFare = fareCalculator.calculateFare(distance, extraFare);

        //then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("요금 정책 - 노선 별 추가금액 계산")
    @ParameterizedTest
    @CsvSource(value = {"0:1250", "100:1350", "1000:2250"}, delimiter = ':')
    void defaultCalculateFareWithExtraFare(int extraFare, int expectedFare) {
        //when
        int actualFare = fareCalculator.calculateFare(1, extraFare);

        //then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("요금 정책 - 연령 별 할인금액 계산")
    @ParameterizedTest
    @CsvSource(value = {"5:0", "6:450", "13:720", "19:1250"}, delimiter = ':')
    void ageCalculateFare(int age, int expectedFare) {
        //given
        AgeDiscountFareCalculator calculator = new AgeDiscountFareCalculator(fareCalculator, age);
        int extraFare = 0;

        //when
        int actualFare = calculator.calculateFare(1, extraFare);

        //then
        assertThat(actualFare).isEqualTo(expectedFare);
    }


}
