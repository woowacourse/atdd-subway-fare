package wooteco.subway.path.domain;

import java.util.ArrayList;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayPathTest {

    @ParameterizedTest(name = "거리 당 추가 요금 계산 테스트")
    @CsvSource(value = {"10:20:1250", "50:20:2050", "90:20:2250"}, delimiter = ':')
    void calculateDistance(int distance, int age, int expected) {

        // given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());

        // when
        final int fare = subwayPath.calculateFare(distance, age);

        // then
        assertThat(fare).isEqualTo(expected);
    }

    @ParameterizedTest(name = "나이 당 요금 할인 계산 테스트")
    @CsvSource(value = {"15:0:0", "15:6:500", "15:13:800", "15:20:1350"}, delimiter = ':')
    void calculateDiscount(int distance, int age, int expected) {

        // given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());

        // when
        final int fare = subwayPath.calculateFare(distance, age);

        // then
        assertThat(fare).isEqualTo(expected);
    }
}
