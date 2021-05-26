package wooteco.subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class LineServiceTest {
    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    @BeforeEach
    void setUp() {
        stationService.saveStation(new StationRequest("잠실역"));
        stationService.saveStation(new StationRequest("잠실새내역"));
        stationService.saveStation(new StationRequest("강남역"));
        stationService.saveStation(new StationRequest("교대역"));
    }

    @DisplayName("중복되는 이름을 가지는 노선을 추가할 시 DuplicateNameException이 발생한다.")
    @Test
    void throw_DuplicateNameException_When_Find_NonExists() {
        lineService.saveLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        assertThatThrownBy(() -> lineService.saveLine(new LineRequest("2호선", "black", 3L, 4L, 20)))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 노선입니다.");
    }
}
