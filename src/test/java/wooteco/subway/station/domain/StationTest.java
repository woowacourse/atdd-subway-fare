package wooteco.subway.station.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.exception.WrongStationNameException;

class StationTest {

    @Test
    @DisplayName("잘못된 이름으로 생성하는 테스트")
    void wrongNameTest() {
        //when
        String name = "역이 아님";

        //then
        assertThatThrownBy(() -> new Station(name))
            .isInstanceOf(WrongStationNameException.class);
    }

}