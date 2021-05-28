package wooteco.subway.line;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LineServiceTest {
    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    @BeforeAll
    void setUp() {
        stationService.saveStation(new StationRequest("잠실역"));
        stationService.saveStation(new StationRequest("잠실새내역"));
        stationService.saveStation(new StationRequest("강남역"));
        stationService.saveStation(new StationRequest("교대역"));
    }

    @DisplayName("중복되는 이름을 가지는 노선을 추가할 시 DuplicateException이 발생한다.")
    @Test
    void throw_DuplicateException_When_Insert_DuplicateName() {
        lineService.saveLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        assertThatThrownBy(() -> lineService.saveLine(new LineRequest("2호선", "black", 3L, 4L, 20)))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("이미 존재하는 노선입니다.");
    }

    @DisplayName("중복되는 색깔을 가지는 노선을 추가할 시 DuplicateException이 발생한다.")
    @Test
    void throw_DuplicateException_When_Insert_DuplicateColor() {
        lineService.saveLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        assertThatThrownBy(() -> lineService.saveLine(new LineRequest("3호선", "bg-red-600", 3L, 4L, 20)))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("이미 존재하는 노선 색깔입니다.");
    }


    @DisplayName("중복되는 이름을 가지는 노선으로 수정할 시 DuplicateException이 발생한다.")
    @Test
    void throw_DuplicateException_When_Update_DuplicateName() {
        lineService.saveLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        LineResponse lineResponse = lineService.saveLine(new LineRequest("4호선", "black", 1L, 2L, 10));
        assertThatThrownBy(() -> lineService.updateLine(lineResponse.getId(), new LineRequest("2호선", "black", 3L, 4L, 20)))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("이미 존재하는 노선입니다.");
    }

    @DisplayName("중복되는 색깔을 가지는 노선으로 수정할 시 DuplicateException이 발생한다.")
    @Test
    void throw_DuplicateException_When_Update_DuplicateColor() {
        lineService.saveLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        LineResponse lineResponse = lineService.saveLine(new LineRequest("4호선", "black", 1L, 2L, 10));
        assertThatThrownBy(() -> lineService.updateLine(lineResponse.getId(), new LineRequest("4호선", "bg-red-600", 3L, 4L, 20)))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("이미 존재하는 노선 색깔입니다.");
    }

    @DisplayName("존재하지 않는 지하철 노선을 검색할 시 NoSuchElementException이 발생한다.")
    @Test
    void throw_NoSuchElementException_When_Find_NonExists() {
        assertThatThrownBy(() -> lineService.findLineById(2L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @DisplayName("존재하지 않는 지하철 노선을 삭제할 시 NoSuchElementException이 발생한다.")
    @Test
    void throw_NoSuchElementException_When_Delete_NonExists() {
        assertThatThrownBy(() -> lineService.deleteLineById(3L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }
}
