package wooteco.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SectionTest {
    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 역삼역 = new Station(2L, "역삼역");

    private Section section;

    @BeforeEach
    void setUp() {
        section = new Section(
                1L, 강남역, 역삼역, 10
        );
    }

    @Test
    void hasSameUpStationId() {
        assertThat(section.hasSameUpStationId(1L)).isTrue();
    }

    @Test
    void hasSameDownStationId() {
        assertThat(section.hasSameDownStationId(2L)).isTrue();
    }

    @Test
    void getId() {
        assertThat(section.getId()).isEqualTo(1L);
    }

    @Test
    void getUpStation() {
        assertThat(section.getUpStation()).isEqualTo(강남역);
    }

    @Test
    void getDownStation() {
        assertThat(section.getDownStation()).isEqualTo(역삼역);
    }

    @Test
    void getDistance() {
        assertThat(section.getDistance()).isEqualTo(10);
    }
}