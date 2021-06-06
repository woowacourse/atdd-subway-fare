package wooteco.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.exception.SectionRegisterException;
import wooteco.subway.line.exception.SectionRemovalException;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.*;

class SectionsTest {

    private Station 테스트역1;
    private Station 테스트역2;
    private Station 테스트역3;
    private Station 테스트역4;
    private Station 테스트역5;

    @BeforeEach
    void setUp() {
        테스트역1 = new Station(1L, "테스트역1");
        테스트역2 = new Station(2L, "테스트역2");
        테스트역3 = new Station(3L, "테스트역3");
        테스트역4 = new Station(4L, "테스트역4");
        테스트역5 = new Station(5L, "테스트역5");
    }

    @Test
    @DisplayName("구간이 서로 이어질 수 있으면 구간을 삽입한다.")
    void addSection() {
        final Section 구간1 = new Section(1L, 테스트역1, 테스트역2, 10);
        final Section 구간2 = new Section(2L, 테스트역2, 테스트역3, 10);
        final Section 구간3 = new Section(3L, 테스트역3, 테스트역4, 10);
        final Section 구간4 = new Section(4L, 테스트역4, 테스트역5, 10);

        final Sections sections = new Sections();
        assertThatCode(() -> sections.addSection(구간1))
                .doesNotThrowAnyException();
        assertThatCode(() -> sections.addSection(구간2))
                .doesNotThrowAnyException();
        assertThatCode(() -> sections.addSection(구간3))
                .doesNotThrowAnyException();
        assertThatCode(() -> sections.addSection(구간4))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("구간 사이의 거리에 구간이 또 들어갈 수 없다면 예외를 출력한다.")
    void addSectionCannotConnect() {
        final Section 구간1 = new Section(1L, 테스트역1, 테스트역3, 10);
        final Section 구간2 = new Section(2L, 테스트역2, 테스트역3, 10);

        final Sections sections = new Sections();
        assertThatCode(() -> sections.addSection(구간1))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> sections.addSection(구간2))
                .isInstanceOf(SectionRegisterException.class);
    }

    @Test
    @DisplayName("새로 추가하려는 구간의 상행역/하행역이 기존 구간들내에 둘 다 없다면 예외를 출력한다")
    void addSectionWhenUpDownStationNotInvolved() {
        final Section 구간1 = new Section(1L, 테스트역1, 테스트역2, 10);
        final Section 구간2 = new Section(2L, 테스트역3, 테스트역4, 10);

        final Sections sections = new Sections();
        assertThatCode(() -> sections.addSection(구간1))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> sections.addSection(구간2))
                .isInstanceOf(SectionRegisterException.class);
    }

    @Test
    @DisplayName("새로 추가하려는 구간의 상행역/하행역이 기존 구간들내에 둘 다 있다면 예외를 출력한다")
    void addSectionWhenUpDownStationAlreadyInvolved() {
        final Section 구간1 = new Section(1L, 테스트역1, 테스트역2, 10);
        final Section 구간2 = new Section(2L, 테스트역2, 테스트역3, 10);
        final Section 구간3 = new Section(2L, 테스트역1, 테스트역3, 10);

        final Sections sections = new Sections();
        assertThatCode(() -> sections.addSection(구간1))
                .doesNotThrowAnyException();
        assertThatCode(() -> sections.addSection(구간2))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> sections.addSection(구간3))
                .isInstanceOf(SectionRegisterException.class);
    }

    @Test
    @DisplayName("구간들이 해당 역을 가지고 있다면 삭제할 수 있다.")
    void removeStation() {
        final Section 구간1 = new Section(1L, 테스트역1, 테스트역2, 10);
        final Section 구간2 = new Section(2L, 테스트역2, 테스트역3, 10);
        final Section 구간3 = new Section(3L, 테스트역3, 테스트역4, 10);
        final Section 구간4 = new Section(4L, 테스트역4, 테스트역5, 10);

        final Sections sections = new Sections();
        sections.addSection(구간1);
        sections.addSection(구간2);
        sections.addSection(구간3);
        sections.addSection(구간4);

        assertThat(sections.getSections()).hasSize(4);
        sections.removeStation(구간1.getDownStation());
        assertThat(sections.getSections()).hasSize(3);
    }

    @Test
    @DisplayName("구간이 하나라면 삭제할 수 없다.")
    void removeStationWhenSectionIsOne() {
        final Section 구간1 = new Section(1L, 테스트역1, 테스트역2, 10);

        final Sections sections = new Sections();
        sections.addSection(구간1);

        assertThatThrownBy(() -> sections.removeStation(구간1.getDownStation()))
                .isInstanceOf(SectionRemovalException.class);
    }

    @Test
    @DisplayName("구간들이 해당 역을 가지고 있는지 검증할 수 있다.")
    void containsStation() {
        final Section 구간1 = new Section(1L, 테스트역1, 테스트역2, 10);
        final Section 구간2 = new Section(2L, 테스트역2, 테스트역3, 10);
        final Section 구간3 = new Section(3L, 테스트역3, 테스트역4, 10);

        final Sections sections = new Sections();
        sections.addSection(구간1);
        sections.addSection(구간2);
        sections.addSection(구간3);

        assertThat(sections.containsStation(테스트역1)).isTrue();
        assertThat(sections.containsStation(테스트역2)).isTrue();
        assertThat(sections.containsStation(테스트역3)).isTrue();
        assertThat(sections.containsStation(테스트역4)).isTrue();
        assertThat(sections.containsStation(테스트역5)).isFalse();
    }
}