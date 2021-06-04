package wooteco.subway.station.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationWithTransferResponse;
import wooteco.subway.station.exception.StationNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("StationRequest를 넘겨받으면, 저장 후 StationResponse를 반환한다.")
    void saveStation() {
        final String stationName = "테스트역";
        final StationResponse response = stationService.saveStation(new StationRequest(stationName));
        assertThat(response.getName()).isEqualTo(stationName);
    }

    @Test
    @DisplayName("id를 넘겨받으면, 해당 id에 대응하는 역을 찾은 후 반환한다.")
    void findStationById() {
        final String stationName = "테스트역";
        final StationResponse response = stationService.saveStation(new StationRequest(stationName));

        final Station stationById = stationService.findStationById(response.getId());
        assertThat(stationById.getName()).isEqualTo(stationName);
    }

    @Test
    @DisplayName("id를 넘겨받은 후, 해당 id에 대응하는 역이 없으면 예외를 출력한다.")
    void findStationByIdException() {
        assertThatThrownBy(() -> stationService.findStationById(100000L))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("현재 DB에 등록된 모든 Station에 대해 StationResponse를 반환한다.")
    void findAllStationResponses() {
        List<StationResponse> allStationResponses = stationService.findAllStationResponses();
        assertThat(allStationResponses.size()).isEqualTo(0);

        stationService.saveStation(new StationRequest("테스트역"));

        allStationResponses = stationService.findAllStationResponses();
        assertThat(allStationResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("DB에 등록되어 있는 Station 중에 노선에 등록된 역이라면, StationWithTransferResponse를 반환한다.")
    void findAllStationWithTransferInfoResponses() {
        List<StationWithTransferResponse> allStationResponses = stationService.findAllStationWithTransferInfoResponses();
        assertThat(allStationResponses.size()).isEqualTo(0);

        StationResponse 테스트역1 = stationService.saveStation(new StationRequest("테스트역1"));
        StationResponse 테스트역2 = stationService.saveStation(new StationRequest("테스트역2"));
        LineResponse 노선 = lineService.saveLine(new LineRequest("노선명", "노선색", 1L, 2L, 100));

        List<StationWithTransferResponse> StationResponses = stationService.findAllStationWithTransferInfoResponses();
        assertThat(StationResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("현재 DB에 등록된 역을 id로 삭제한다")
    void deleteStationById() {
        final StationResponse 테스트역 = stationService.saveStation(new StationRequest("테스트역"));

        stationService.deleteStationById(테스트역.getId());

        assertThatThrownBy(() -> stationService.deleteStationById(테스트역.getId()))
                .isInstanceOf(StationNotFoundException.class);
    }
}