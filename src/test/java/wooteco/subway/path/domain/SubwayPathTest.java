package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

class SubwayPathTest {
    private SubwayPath subwayPath;

    private Line line = new Line("2호선", "black");
    private Station station1 = new Station("강남역");
    private Station station2 = new Station("잠실역");
    private Station station3 = new Station("교대역");

    @DisplayName("기본 운임인 경우")
    @Test
    void defaultDistanceFare() {
        SectionEdge sectionEdge1 = new SectionEdge(new Section(station1, station2, 5), line);
        SectionEdge sectionEdge2 = new SectionEdge(new Section(station2, station3, 5), line);

        subwayPath = new SubwayPath(Arrays.asList(sectionEdge1, sectionEdge2),
            Arrays.asList(station1, station2, station3), 0);
        assertThat(subwayPath.calculateFareByDistance()).isEqualTo(1250);
    }

    @DisplayName("50키로 이하인 경우")
    @Test
    void middleDistanceFare() {
        SectionEdge sectionEdge1 = new SectionEdge(new Section(station1, station2, 25), line);
        SectionEdge sectionEdge2 = new SectionEdge(new Section(station2, station3, 25), line);

        subwayPath = new SubwayPath(Arrays.asList(sectionEdge1, sectionEdge2),
            Arrays.asList(station1, station2, station3), 0);
        assertThat(subwayPath.calculateFareByDistance()).isEqualTo(2050);
    }

    @DisplayName("50키로 초과인 경우")
    @Test
    void extraDistanceFare() {
        SectionEdge sectionEdge1 = new SectionEdge(new Section(station1, station2, 30), line);
        SectionEdge sectionEdge2 = new SectionEdge(new Section(station2, station3, 30), line);

        subwayPath = new SubwayPath(Arrays.asList(sectionEdge1, sectionEdge2),
            Arrays.asList(station1, station2, station3), 0);
        assertThat(subwayPath.calculateFareByDistance()).isEqualTo(2450);
    }

    @DisplayName("환승이 없는 경우에 요금이 추가되는 경우")
    @Test
    void oneLineExtraFare() {
        Section section1 = new Section(station1, station2, 30);
        Section section2 = new Section(station2, station3, 30);
        Sections sections = new Sections(Arrays.asList(section1, section2));
        line = new Line(1L, "testLine1", "black", sections, 900);

        SectionEdge sectionEdge1 = new SectionEdge(section1, line);
        SectionEdge sectionEdge2 = new SectionEdge(section2, line);

        subwayPath = new SubwayPath(Arrays.asList(sectionEdge1, sectionEdge2),
            Arrays.asList(station1, station2, station3), 0);
        assertThat(subwayPath.calculateFareByDistance()).isEqualTo(3350);
    }

    @DisplayName("환승이 있는 경우에 요금이 추가되는 경우")
    @Test
    void twoLineExtraFare() {
        Section section1 = new Section(station1, station2, 30);
        Section section2 = new Section(station2, station3, 30);
        Sections sections1 = new Sections(Collections.singletonList(section1));
        Sections sections2 = new Sections(Collections.singletonList(section2));
        line = new Line(1L, "testLine1", "black", sections1, 900);
        Line line1 = new Line(2L , "testLine2", "white", sections2, 1000);
        SectionEdge sectionEdge1 = new SectionEdge(section1, line);
        SectionEdge sectionEdge2 = new SectionEdge(section2, line1);

        subwayPath = new SubwayPath(Arrays.asList(sectionEdge1, sectionEdge2),
            Arrays.asList(station1, station2, station3), 0);
        assertThat(subwayPath.calculateFareByDistance()).isEqualTo(3450);
    }
}