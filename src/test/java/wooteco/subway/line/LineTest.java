package wooteco.subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    Station 양재역 = new Station(2L, "양재역");;
    Station 교대역 = new Station(3L, "교대역");;
    Station 남부터미널역 = new Station(4L, "남부터미널역");;

    Line 삼호선 = new Line("삼호선", "brown");

    @BeforeEach
    void setUp() {
        삼호선.addSection(교대역, 남부터미널역, 3);
        삼호선.addSection(남부터미널역, 양재역, 2);
    }

    @Test
    @DisplayName("노선의 길이를 잘 반환하는지 확인한다.")
    void totalDistance() {
        assertThat(삼호선.distance()).isEqualTo(5);
    }
}
