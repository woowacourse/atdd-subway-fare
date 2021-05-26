package wooteco.subway.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareTest {
    
    @DisplayName("50km 초과일 때")
    @ParameterizedTest
    @CsvSource(value = {"51,19,0,2150","59,19,100,2350","67,18,0,1950","67,13,100,2050",
                        "90,12,0,1450","100,6,100,1650","51,5,0,2150", "58,20,0,2150"})
    public void longDistanceCase(int distance, int age, int extraFee, int expected) {
        Fare fare = new Fare(distance, extraFee, age);
        assertThat(fare.getFare()).isEqualTo(expected);
    }

    @DisplayName("10km이상, 51km미만 일 때")
    @ParameterizedTest
    @CsvSource(value = {"50,19,0,2050","46,18,100,1810","35,13,0,1470",
                        "30,12,100,1100","29,6,0,1000","11,5,100,1450"})
    public void middleDistanceCase(int distance, int age, int extraFee, int expected) {
        Fare fare = new Fare(distance, extraFee, age);
        assertThat(fare.getFare()).isEqualTo(expected);
    }

    @DisplayName("기본 요금일 경우")
    @ParameterizedTest
    @CsvSource(value = {"19,0,1250","18,100,1170","13,0,1070",
            "12,100,900","6,0,800","5,100,1350"})
    public void basicDistanceCase(int age, int extraFee, int expected) {
        Fare fare = new Fare(10, extraFee, age);
        assertThat(fare.getFare()).isEqualTo(expected);
    }
}