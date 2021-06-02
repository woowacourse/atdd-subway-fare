package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {

    private SubwayFare subwayFare;

    @BeforeEach
    void setUp() {
        subwayFare = new SubwayFare(new ArrayList<>());
    }

    @DisplayName("거리에 따른 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "21:1550", "50:2050", "51:2150", "59:2250"}, delimiterString = ":")
    void calculateFareByDistance(int distance, int fare) {
        assertThat(subwayFare.calculateFare(distance, 30)).isEqualTo(fare);
    }

    @DisplayName("나이에 따른 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"0:0", "6:800", "13:1070", "17:1070", "19:1250", "21:1250"}, delimiterString = ":")
    void calculateFareByAge(int age, int fare) {
        assertThat(subwayFare.calculateFare(5, age)).isEqualTo(fare);
    }
}
