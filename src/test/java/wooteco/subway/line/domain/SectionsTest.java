package wooteco.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

class SectionsTest {

    private final Station 상봉역 = new Station(1L, "상봉역");
    private final Station 면목역 = new Station(2L, "면목역");
    private final Station 사가정역 = new Station(3L, "사가정역");
    private final Station 중곡역 = new Station(4L, "중곡역");
    private final Station 용마산역 = new Station(5L, "용마산역");
    private final Station 어린이대공원역 = new Station(6L, "어린이대공원역");
    private final Station 건대입구역 = new Station(7L, "건대입구역");

    private final Section 상봉역_면목역 = new Section(1L, 상봉역, 면목역, 5);
    private final Section 면목역_사가정역 = new Section(1L, 면목역, 사가정역, 9);
    private final Section 사가정역_중곡역 = new Section(1L, 사가정역, 중곡역, 10);
    private final Section 중곡역_용마산역 = new Section(1L, 중곡역, 용마산역, 15);
    private final Section 용마산역_어린이대공원역 = new Section(1L, 용마산역, 어린이대공원역, 20);
    private final Section 어린이대공원역_건대입구역 = new Section(1L, 어린이대공원역, 건대입구역, 24);

    @Test
    @DisplayName("노선에 속한 구간의 전체 구간 길이 반환")
    void getTotalDistance() {
        // given
        Sections sections = new Sections();
        sections.addSection(상봉역_면목역);
        sections.addSection(면목역_사가정역);
        sections.addSection(사가정역_중곡역);
        sections.addSection(중곡역_용마산역);
        sections.addSection(용마산역_어린이대공원역);
        sections.addSection(어린이대공원역_건대입구역);

        // when
        int distance = sections.getTotalDistance();

        // then
        assertThat(distance).isEqualTo(
            상봉역_면목역.getDistance()
                + 면목역_사가정역.getDistance()
                + 사가정역_중곡역.getDistance()
                + 중곡역_용마산역.getDistance()
                + 용마산역_어린이대공원역.getDistance()
                + 어린이대공원역_건대입구역.getDistance()
        );
    }

}