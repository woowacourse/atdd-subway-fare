package wooteco.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.application.PathFinder;
import wooteco.subway.station.domain.Station;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayPathTest {



    @ParameterizedTest(name = "거리 당 추가 요금 계산 테스트")
    @CsvSource(value = {"10:20:1250", "50:20:2050", "90:20:2250"}, delimiter = ':')
    void calculateDistance(int distance, int age, int expected) {

        // given
        Station station1 = new Station(1L, "1");
        Station station2 = new Station(2L, "2");
        Section section = new Section(1L, station1, station2, distance);
        Line line = new Line(1L, "line", "red", 0, new Sections(Collections.singletonList(section)));
        SubwayPath subwayPath = new PathFinder().findPath(Collections.singletonList(line), station1, station2);

        // when
        final int fare = subwayPath.calculateFare(age);

        // then
        assertThat(fare).isEqualTo(expected);
    }

    @ParameterizedTest(name = "나이 당 요금 할인 계산 테스트")
    @CsvSource(value = {"15:0:0", "15:6:500", "15:13:800", "15:20:1350"}, delimiter = ':')
    void calculateDiscount(int distance, int age, int expected) {

        // given
        Station station1 = new Station(1L, "1");
        Station station2 = new Station(2L, "2");
        Section section = new Section(1L, station1, station2, distance);
        Line line = new Line(1L, "line", "red", 0, new Sections(Collections.singletonList(section)));
        SubwayPath subwayPath = new PathFinder().findPath(Collections.singletonList(line), station1, station2);

        // when
        final int fare = subwayPath.calculateFare(age);

        // then
        assertThat(fare).isEqualTo(expected);
    }
}
