package wooteco.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SectionsTest {
    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 역삼역 = new Station(2L, "역삼역");
    private static final Station 선릉역 = new Station(3L, "선릉역");

    private Sections sections;

    @BeforeEach
    void setUp() {
        sections = new Sections(
                Arrays.asList(
                        new Section(1L, 강남역, 역삼역, 10),
                        new Section(2L, 역삼역, 선릉역, 20)
                )
        );
    }

    @Test
    void empty() {
        assertThat(Sections.empty().getSections()).hasSize(0);
        assertThat(Sections.empty().getStations()).hasSize(0);
    }

    @Test
    void addSection() {
        Station 모란역 = new Station(4L, "모란역");
        sections.addSection(new Section(3L, 선릉역, 모란역, 10));
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(
                        Arrays.asList(
                                new Section(1L, 강남역, 역삼역, 10),
                                new Section(2L, 역삼역, 선릉역, 20),
                                new Section(3L, 선릉역, 모란역, 10)
                        )
                );
    }

    @Test
    void getStations() {
        assertThat(sections.getStations()).containsExactly(
                강남역, 역삼역, 선릉역
        );
    }

    @Test
    void removeStation() {
        sections.removeStation(역삼역);

        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(
                        Arrays.asList(
                                new Section(null, 강남역, 선릉역, 30)
                        )
                );

        assertThat(sections.getStations())
                .containsExactly(강남역, 선릉역);
    }

    @Test
    void updateDistance() {
        sections.updateDistance(1L, 2L, 50);

        Section changedSection = sections.getSections().stream()
                .filter(section -> section.getUpStation().getId().equals(1L))
                .filter(section -> section.getDownStation().getId().equals(2L))
                .findAny()
                .orElseThrow(AssertionError::new);

        assertThat(changedSection.getDistance()).isEqualTo(50);
    }

    @Test
    void getSections() {
        assertThat(sections.getSections())
                .usingRecursiveComparison()
                .isEqualTo(
                        Arrays.asList(
                                new Section(1L, 강남역, 역삼역, 10),
                                new Section(2L, 역삼역, 선릉역, 20)
                        )
                );
    }
}