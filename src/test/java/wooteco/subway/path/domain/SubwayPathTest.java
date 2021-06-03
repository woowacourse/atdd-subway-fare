package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {

    private SubwayPath subwayPath;

    /**
     * 1호선
     * A역 -- 3 -- B역
     */
    @BeforeEach
    void setUp() {
        Station station1 = new Station("1역");
        Station station2 = new Station("2역");
        Station station3 = new Station("3역");
        Section section1 = new Section(station1, station2, 2);
        Section section2 = new Section(station2, station3, 3);
        Line line = new Line(1L, "1호선", "red", 0, new Sections(Arrays.asList(section1, section2)));

        List<Station> stations = Arrays.asList(station1, station2);
        List<SectionEdge> sectionEdges = Arrays.asList(new SectionEdge(section1, line), new SectionEdge(section2, line));

        subwayPath = new SubwayPath(sectionEdges, stations);
    }

    @DisplayName("경로의 거리를 반환한다.")
    @Test
    void calculateDistance() {
        // when
        int distance = subwayPath.calculateDistance();

        // then
        assertThat(distance).isEqualTo(5);
    }

    @DisplayName("경로 거리에 따른 요금을 반환한다.")
    @Test
    void calculateFare() {
        // when
        int fare = subwayPath.calculateFare();

        // then
        assertThat(fare).isEqualTo(1250);
    }
}