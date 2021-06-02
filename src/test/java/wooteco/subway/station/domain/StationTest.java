package wooteco.subway.station.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.exception.InvalidStationNameException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationTest {

    @Test
    @DisplayName("역의 이름은 2자 이상, 공백이 없는 한글/숫자로 구성되어야 한다")
    public void stationCreate() {
        assertThatThrownBy(() -> new Station("english"))
                .isInstanceOf(InvalidStationNameException.class);

        assertThatThrownBy(() -> new Station("한"))
                .isInstanceOf(InvalidStationNameException.class);

        assertThatThrownBy(() -> new Station("공 백"))
                .isInstanceOf(InvalidStationNameException.class);

        assertThatThrownBy(() -> new Station("@@@##$"))
                .isInstanceOf(InvalidStationNameException.class);
    }
}