package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathFareTest {

    @ParameterizedTest
    @CsvSource({"5,10,100,0", "10,30,100,700", "14,50,200,1520", "19,30,100,1750"})
    @DisplayName("할인 정책이 적용된 요금을 구한다.")
    void getFare(int age, int distance, int lineFare,int expected) {
        int result = new SubwayPathFare(age, distance, lineFare).getFare();

        assertThat(result).isEqualTo(expected);
    }
}