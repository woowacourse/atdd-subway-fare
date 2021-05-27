package wooteco.subway.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext
public class StationServiceTest {
    @Autowired
    private StationService stationService;

    @DisplayName("중복되는 이름을 가지는 역을 추가할 시 DuplicateNameException이 발생한다.")
    @Test
    void throw_DuplicateNameException_When_Find_NonExists() {
        stationService.saveStation(new StationRequest("잠실역"));
        assertThatThrownBy(() -> stationService.saveStation(new StationRequest("잠실역")))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 역입니다.");
    }

    @DisplayName("존재하지 않는 지하철 역을 검색할 시 NoSuchElementException이 발생한다.")
    @Test
    void throw_NoSuchElementException_When_Find_NonExists() {
        assertThatThrownBy(() -> stationService.findStationById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }

    @DisplayName("존재하지 않는 지하철 역을 삭제할 시 NoSuchElementException이 발생한다.")
    @Test
    void throw_NoSuchElementException_When_Delete_NonExists() {
        assertThatThrownBy(() -> stationService.deleteStationById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }
}
