package wooteco.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;


class LineTest {

    @Test
    @DisplayName("노선에 역이 존재하는지를 검증한다.")
    void containsStation() {
        Station 테스트역1 = new Station(1L, "테스트역1");
        Station 테스트역2 = new Station(1L, "테스트역2");
        Station 테스트역3 = new Station(1L, "테스트역3");
        Section 구간 = new Section(1L, 테스트역1, 테스트역2, 200);
        Line 테스트선 = new Line(1L, "테스트선", "테스트색", 1000, new Sections(Collections.singletonList(구간)));

        assertThat(테스트선.containsStation(테스트역1)).isTrue();
        assertThat(테스트선.containsStation(테스트역2)).isTrue();
        assertThat(테스트선.containsStation(테스트역3)).isFalse();
    }
}