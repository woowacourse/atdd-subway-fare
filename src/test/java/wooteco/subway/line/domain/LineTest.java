package wooteco.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

class LineTest {

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

    private Sections sections;

    @BeforeEach
    void setUp() {
        sections = new Sections(Arrays.asList(
            상봉역_면목역,
            면목역_사가정역,
            사가정역_중곡역,
            중곡역_용마산역,
            용마산역_어린이대공원역,
            어린이대공원역_건대입구역
        ));
    }

    @Test
    @DisplayName("노선의 상행선 종착역 반환")
    void getStartStation() {
        // given
        Line line = new Line(1L, "7호선", "bg-green-100", sections);

        // when
        Station startStation = line.getStartStation();

        // then
        assertThat(startStation).isEqualTo(상봉역);
    }

    @Test
    @DisplayName("노선의 하행선 종착역 반환")
    void getEndStation() {
        // given
        Line line = new Line(1L, "7호선", "bg-green-100", sections);

        // when
        Station endStation = line.getEndStation();

        // then
        assertThat(endStation).isEqualTo(건대입구역);
    }
}