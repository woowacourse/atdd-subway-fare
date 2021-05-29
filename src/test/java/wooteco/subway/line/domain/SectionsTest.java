package wooteco.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.station.StationFixture.*;

class SectionsTest {

    @DisplayName("전체 거리 계산")
    @Test
    void totalDistance() {
        // given
        Sections sections = new Sections(Arrays.asList(
                new Section(잠실역, 강남역, 5),
                new Section(강남역, 구의역, 7),
                new Section(왕십리역, 잠실역, 10)
        ));

        // when
        int distance = sections.totalDistance();

        // then
        assertThat(distance).isEqualTo(22);
    }
}