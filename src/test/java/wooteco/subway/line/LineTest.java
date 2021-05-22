package wooteco.subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineFixture.이호선;
import static wooteco.subway.station.StationFixture.*;

public class LineTest {

    @BeforeEach
    void setUp() {
        이호선.addSection(강남역, 잠실역, 3);
        이호선.addSection(잠실역, 구의역, 2);
    }

    @Test
    @DisplayName("노선의 길이를 잘 반환하는지 확인한다.")
    void totalDistance() {
        assertThat(이호선.distance()).isEqualTo(5);
    }
}
