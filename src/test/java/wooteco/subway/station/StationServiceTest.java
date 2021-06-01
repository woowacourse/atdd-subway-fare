package wooteco.subway.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class StationServiceTest {
    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @DisplayName("중복되는 이름을 가지는 역을 추가할 시 DuplicateNameException이 발생한다.")
    @Test
    void throw_DuplicateNameException_When_Insert_DuplicateName() {
        stationService.saveStation(new StationRequest("잠실역"));
        assertThatThrownBy(() -> stationService.saveStation(new StationRequest("잠실역")))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("이미 존재하는 역입니다.");
    }

    @DisplayName("존재하지 않는 지하철 역을 검색할 시 NoSuchElementException이 발생한다.")
    @Test
    void throw_NoSuchElementException_When_Find_NonExists() {
        assertThatThrownBy(() -> stationService.findStationById(3L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }

    @DisplayName("존재하지 않는 지하철 역을 삭제할 시 NoSuchElementException이 발생한다.")
    @Test
    void throw_NoSuchElementException_When_Delete_NonExists() {
        assertThatThrownBy(() -> stationService.deleteStationById(3L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }

    @DisplayName("노선에 등록되어 있는 역을 삭제할 시 UnsupportedOperationException이 발생한다.")
    @Test
    void throw_UnsupportedOperationException_When_Delete_RegisteredStation() {
        StationResponse 원인재역 = stationService.saveStation(new StationRequest("원인재역"));
        StationResponse 신연수역 = stationService.saveStation(new StationRequest("신연수역"));
        lineService.saveLine(new LineRequest("2호선", "black", 원인재역.getId(), 신연수역.getId(), 10));

        assertThatThrownBy(() ->  stationService.deleteStationById(원인재역.getId()))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("노선에 등록된 역은 삭제할 수 없습니다.");
    }
}
