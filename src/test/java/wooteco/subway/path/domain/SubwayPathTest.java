package wooteco.subway.path.domain;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayPathTest {

    @ParameterizedTest(name = "요금 계산 테스트")
    @CsvSource(value = {"10:1250", "50:2050", "90:2250"}, delimiter = ':')
    void calculateDistance(int given, int expected) {

        // given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());

        // when
        final int fare = subwayPath.calculateFare(given);

        // then
        assertThat(fare).isEqualTo(expected);
    }
}
