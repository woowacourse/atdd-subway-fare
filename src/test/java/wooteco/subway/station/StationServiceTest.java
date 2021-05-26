package wooteco.subway.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class StationServiceTest {
    @Autowired
    private StationService stationService;

    @DisplayName("StationDao에서 반환하는 DuplicateKeyException을 커스텀 예외인 DuplicateNameException으로 변환한다.")
    @Test
    void test() {
        stationService.saveStation(new StationRequest("잠실역"));
        assertThatThrownBy(() -> stationService.saveStation(new StationRequest("잠실역")))
                .isInstanceOf(DuplicateNameException.class);
    }
}
