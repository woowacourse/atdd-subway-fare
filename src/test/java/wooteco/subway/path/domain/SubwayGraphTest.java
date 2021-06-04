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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayGraphTest {
    private Station station1;
    private Station station2;
    private Station station3;
    private Section section1;
    private Section section2;
    private Sections sections;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, "강남역");
        station2 = new Station(2L, "선릉역");
        station3 = new Station(3L, "역삼역");

        section1 = new Section(1L, station1, station2, 20);
        section2 = new Section(2L, station2, station3, 30);
        sections = new Sections(Arrays.asList(section1, section2));
    }

    @DisplayName("지하철 경로 그래프 노선들을 기준으로 꼭지점을 추가한다.")
    @Test
    void addVertexWith() {
        //given
        Line line = new Line(1L, "1호선", "빨간색", sections);
        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.class);

        //when
        subwayGraph.addVertexWith(Collections.singletonList(line));
        Set<Station> stations = subwayGraph.vertexSet();

        //then
        assertThat(stations).containsExactly(station1, station2, station3);
    }

    @DisplayName("노선들을 기준으로 지하철 구간 엣지를 여러개 등록한다")
    @Test
    void addEdge() {
        //given
        Line line1 = new Line(1L, "1호선", "빨간색", new Sections(Collections.singletonList(section1)));
        Line line2 = new Line(2L, "2호선", "파란색", new Sections(Collections.singletonList(section2)));
        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.class);
        subwayGraph.addVertexWith(Arrays.asList(line1, line2));

        //when
        subwayGraph.addEdge(Arrays.asList(line1, line2));

        //then
        Set<SectionEdge> sectionEdges = subwayGraph.edgeSet();
        assertThat(sectionEdges).hasSize(2);
    }
}