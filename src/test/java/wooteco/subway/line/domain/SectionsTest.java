package wooteco.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.NotPermittedException;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SectionsTest {

    private static final Station UP_STATION = new Station(1L, "조앤");
    private static final Station NEW_UP_STATION = new Station(3L, "조w앤");
    private static final Station DOWN_STATION = new Station(2L, "이사");
    private static final Station NEW_DOWN_STATION = new Station(4L, "이2사");
    private static final Section SECTION = new Section(1L, UP_STATION, DOWN_STATION, 10);
    private static final Section SECTION_FOR_ADD = new Section(2L, UP_STATION, NEW_DOWN_STATION, 7);
    private static final Section SECTION_WITH_INVALID_DISTANCE = new Section(3L, UP_STATION, NEW_DOWN_STATION, 100);
    private static final Section SECTION_WITH_INVALID_ENDPOINTS = new Section(4L, NEW_UP_STATION, NEW_DOWN_STATION, 6);

    Sections sections;

    @BeforeEach
    void setUp() {
        List<Section> sectionInputs = new ArrayList<>();
        sectionInputs.add(SECTION);
        sections = new Sections(sectionInputs);
    }

    @Test
    @DisplayName("새로운 구간을 추가한다.")
    void addSection() {
        assertThatCode(() -> sections.addSection(SECTION_FOR_ADD))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("양 끝 역이 존재하지 않는 구간을 추가하면 예외를 던진다.")
    void addSectionWithInvalidEndpoints() {
        assertThatThrownBy(() -> sections.addSection(SECTION_WITH_INVALID_ENDPOINTS))
                .isInstanceOf(NotPermittedException.class);
    }

    @Test
    @DisplayName("유효하지 않은 거리의 구간을 추가하면 예외를 던진다.")
    void addSectionWithInvalidDistance() {
        assertThatThrownBy(() -> sections.addSection(SECTION_WITH_INVALID_DISTANCE))
                .isInstanceOf(NotPermittedException.class);
    }

    @Test
    @DisplayName("구간에 추가된 역을 가져온다.")
    void getStations() {
        final List<Station> stations = sections.getStations();
        assertThat(stations.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("구간에 추가된 역을 삭제한다.")
    void removeStation() {
        sections.addSection(SECTION_FOR_ADD);
        assertThatCode(() -> sections.removeStation(UP_STATION))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("구간이 하나인 경우 역을 삭제하면 예외를 던진다.")
    void removeStationWhenSectionSizeIsLessThanOne() {
        assertThatThrownBy(() -> sections.removeStation(UP_STATION))
                .isInstanceOf(NotPermittedException.class);
    }
}