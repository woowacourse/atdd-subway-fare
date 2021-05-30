package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {
    @DisplayName("더하기")
    @ParameterizedTest
    @CsvSource({"100,500,600", "200,530,730", "10000,12345,22345", "0,100,100"})
    void addTest(int before, int after, int expectedFare) {
        //given
        Fare fare1 = new Fare(before);
        Fare fare2 = new Fare(after);
        //when
        Fare add = fare1.add(fare2);
        //then
        assertThat(add).isEqualTo(new Fare(expectedFare));
    }

    @DisplayName("빼기")
    @ParameterizedTest
    @CsvSource({"500,100,400", "600,530,70", "12345,10000,2345", "100,0,100"})
    void minusTest(int before, int after, int expectedFare) {
        //given
        Fare fare1 = new Fare(before);
        Fare fare2 = new Fare(after);
        //when
        Fare minus = fare1.minus(fare2);
        //then
        assertThat(minus).isEqualTo(new Fare(expectedFare));
    }

    @DisplayName("곱셈")
    @ParameterizedTest
    @CsvSource({"500, 1.5, 750", "1000, 0.8, 800", "1234, 0.9, 1110"})
    void multiplyTest(int fareAmount, double ratio, int expectedFare) {
        //given
        Fare fare = new Fare(fareAmount);
        //when
        Fare multiply = fare.multiply(ratio);
        //then
        assertThat(multiply).isEqualTo(new Fare(expectedFare));
    }

}