package wooteco.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {

    @ParameterizedTest
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "21:1550", "50:2050", "51:2150", "59:2250"}, delimiterString = ":")
    void calculateFare(int distance, int fare) {
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());

        assertThat(subwayPath.calculateFare(distance)).isEqualTo(fare);
    }
}
