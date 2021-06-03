package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {

    private SubwayPath subwayPath;

    /**
     * 1호선 총5km
     * [1역] <- 2km -> [2역] <- 3km -> [3역]
     */
    @BeforeEach
    void setUp() {
        Station 역1 = new Station("1역");
        Station 역2 = new Station("2역");
        Station 역3 = new Station("3역");
        Section 구간1 = new Section(역1, 역2, 2);
        Section 구간2 = new Section(역2, 역3, 3);
        Line 노선 = new Line(1L, "1호선", "red", 0, new Sections(Arrays.asList(구간1, 구간2)));

        List<Station> stations = Arrays.asList(역1, 역2);
        List<SectionEdge> sectionEdges = Arrays.asList(new SectionEdge(구간1, 노선), new SectionEdge(구간2, 노선));

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