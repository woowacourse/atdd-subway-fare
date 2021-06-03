package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.application.PathFinder;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayPathTest {

    private final PathFinder pathFinder = new PathFinder();
    private Station 강남역 = new Station(1L, "강남역");
    private Station 잠실역 = new Station(2L, "잠실역");
    private Station 송파역 = new Station(3L, "송파역");

    @DisplayName("현재 경로에 포함된 노선들 중 가장 높은 노선 추가 요금을 확인한다.")
    @Test
    void calculateLineMaxFare() {
        Section section1 = new Section(1L, 강남역, 잠실역, 5);
        Section section2 = new Section(2L, 잠실역, 송파역, 5);
        Sections sections1 = new Sections(Arrays.asList(section1));
        Sections sections2 = new Sections(Arrays.asList(section2));
        Line 이호선 = new Line(1L, "2호선", "black", 900, sections1);
        Line 삼호선 = new Line(2L, "3호선", "white", 300, sections2);

        SubwayPath subwayPath = pathFinder.findPath(Arrays.asList(이호선, 삼호선), 강남역, 송파역);
        int lineExtraFare = subwayPath.calculateLineMaxFare();

        assertThat(lineExtraFare).isEqualTo(900);
    }
}
