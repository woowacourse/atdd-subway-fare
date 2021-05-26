package wooteco.subway.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class StationServiceTest {
    @Autowired
    private StationService stationService;

    @DisplayName("StationDao에서 반환하는 DuplicateKeyException을 커스텀 예외인 DuplicateNameException으로 변환한다.")
    @Test
    void duplicateNameExceptionTest() {
        stationService.saveStation(new StationRequest("잠실역"));
        assertThatThrownBy(() -> stationService.saveStation(new StationRequest("잠실역")))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 역입니다.");
    }

    @DisplayName("존재하지 않는 지하철 역을 삭제할 시 NoSuchElementException이 발생한다.")
    @Test
    void noSuchElementExceptionTest() {
        assertThatThrownBy(() -> stationService.deleteStationById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }
}
