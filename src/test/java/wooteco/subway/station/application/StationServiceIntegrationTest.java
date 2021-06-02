package wooteco.subway.station.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.IntegrationTest;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;


class StationServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private StationService stationService;

    @Test
    @DisplayName("역 추가 테스트")
    void saveStation() {
        //given
        String name = "신도림역";
        StationRequest stationRequest = new StationRequest(name);

        //when
        StationResponse savedStationResponse = stationService.saveStation(stationRequest);

        //then
        assertThat(savedStationResponse.getName()).isEqualTo(name);
        assertThat(savedStationResponse.getId()).isEqualTo(4);
    }

    @Test
    @DisplayName("역 조회 테스트")
    void showStation() {
        //given
        Long id = 1L;
        String name = "신설동역";

        //when
        Station station = stationService.findStationById(id);

        //then
        assertThat(station.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("모든 역 조회 테스트")
    void showStations() {
        //given
        int stationsNumber = 3;

        //when
        List<StationResponse> stationResponses = stationService.findAllStationResponses();

        //then
        assertThat(stationResponses.size()).isEqualTo(stationsNumber);
    }

    @Test
    @DisplayName("역 삭제 테스트")
    void deleteStation() {
        //given
        int originalStationsNumber = 3;
        Long predictedId = 4L;
        String name = "신도림역";
        StationRequest stationRequest = new StationRequest(name);
        StationResponse savedStationResponse = stationService.saveStation(stationRequest);

        //when
        stationService.deleteStationById(4L);
        List<StationResponse> stationResponses = stationService.findAllStationResponses();

        //then
        assertThat(stationResponses.size()).isEqualTo(originalStationsNumber);
    }

}