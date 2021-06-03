package wooteco.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.badrequest.BadRequest;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SectionsTest {
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
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section1);
        sectionList.add(section2);
        sections = new Sections(sectionList);
    }

    @DisplayName("구간들을 가지는 일급 컬렉션 생성한다.")
    @Test
    void create() {
        //given
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(section1);
        sectionList.add(section2);

        //when
        Sections sections = new Sections(sectionList);

        //then
        assertThat(sections).isInstanceOf(Sections.class);
        assertThat(sections.getSections()).hasSize(2);
        assertThat(sections.getSections()).containsExactly(section1, section2);
    }

    @DisplayName("구간 추가 한다.")
    @Test
    void addSection() {
        //given
        Station station4 = new Station(4L, "잠실역");
        Section section = new Section(station3, station4, 1);

        //when
        sections.addSection(section);

        //then
        assertThat(sections.getSections()).hasSize(3);
        assertThat(sections.getSections()).containsExactly(section1, section2, section);
    }

    @DisplayName("이미 존재하는 구간 추가 한다.")
    @Test
    void addDuplicateSection() {
        //when then
        assertThatThrownBy(() -> sections.addSection(section2))
            .isInstanceOf(BadRequest.class);
    }

    @DisplayName("상행역과 하행역이 같은 구간을 추가 한다.")
    @Test
    void addSameStationSection() {
        //given
        Section section = new Section(station1, station1, 10);

        //when then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(BadRequest.class);
    }

    @DisplayName("구간의 순서로 정렬된 역들을 가져온다.")
    @Test
    void getStations() {
        //when
        List<Station> stations = sections.getStations();

        //then
        assertThat(stations).hasSize(3);
        assertThat(stations).containsExactly(station1, station2, station3);
    }

    @DisplayName("구간의 순서로 정렬된 구간들을 가져온다.")
    @Test
    void getSortSections() {
        //when
        List<Section> sortSections = sections.getSortSections();

        //then
        assertThat(sortSections).hasSize(2);
        assertThat(sortSections).containsExactly(section1, section2);
    }

    @DisplayName("구간에 존재하는 역을 삭제한다.")
    @Test
    void removeStation() {
        //when
        sections.removeStation(station2);

        //then
        assertThat(sections.getSections()).hasSize(1);
        assertThat(sections.getStations()).hasSize(2);
    }

    @DisplayName("구간에 존재하지 않는 역을 삭제한다.")
    @Test
    void removeStationByNotExistStation() {
        //given
        Station station4 = new Station(4L, "잠실역");

        //when
        assertThatThrownBy(() -> sections.removeStation(station4))
            .isInstanceOf(BadRequest.class);
    }
}