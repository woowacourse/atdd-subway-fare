package wooteco.subway.station.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationTest {

    @DisplayName("지하철 역 객체 생성된다.")
    @Test
    void create() {
        //when
        Station station = new Station(1L, "1호선");

        //then
        assertThat(station).isInstanceOf(Station.class);
        assertThat(station).isEqualTo(new Station(1L, "1호선"));
    }

    @DisplayName("Id가 null인 지하철 역 객체 생성된다.")
    @Test
    void createWithoutId() {
        //when
        Station station = new Station("1호선");

        //then
        assertThat(station).isInstanceOf(Station.class);
    }

    @DisplayName("지하철 이름이 null인 지하철 역 객체 생성하면 예외 발생한다..")
    @Test
    void createWithoutName() {
        //when then
        assertThatThrownBy(() -> new Station(1L, null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Station(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("지하철 역 정보 수정된다.")
    @Test
    void update() {
        //given
        Station station = new Station(1L, "1호선");

        //when
        Station updatedStation = station.update("2호선");

        //then
        assertThat(station.getName()).isEqualTo("1호선");
        assertThat(updatedStation.getId()).isEqualTo(station.getId());
        assertThat(updatedStation.getName()).isEqualTo("2호선");
    }
}