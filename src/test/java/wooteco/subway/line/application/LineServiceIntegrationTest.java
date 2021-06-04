package wooteco.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.IntegrationTest;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;

class LineServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private StationService stationService;
    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("노선 추가")
    void saveLine() {
        //given
        String name = "2호선";
        String color = "초록색";
        Long upStationId = 2L;
        Long downStationId = 3L;
        int distance = 10;
        int extraFare = 100;
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance,
            extraFare);

        //then
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        //then
        assertThat(lineResponse.getId()).isEqualTo(2L);
        assertThat(lineResponse.getName()).isEqualTo(name);
        assertThat(lineResponse.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("노선 찾기")
    void showLineById() {
        //given
        Long id = 1L;
        String name = "1호선";
        String color = "파란색";
        int extraFare = 0;

        //when
        Line line = lineService.findLineById(1L);

        //then
        assertThat(line.getName()).isEqualTo(name);
        assertThat(line.getColor()).isEqualTo(color);
        assertThat(line.getExtraFare()).isEqualTo(0);
        assertThat(line.getStations().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("노선 업데이트")
    void updateLine() {
        //given
        Long id = 1L;
        String name = "1호선";
        String color = "파란색";
        int distance = 100;
        int extraFare = 200;
        LineRequest lineUpdateRequest = new LineRequest(name, color, 2L, 3L, distance, extraFare);

        //when
        lineService.updateLine(id, lineUpdateRequest);
        Line line = lineService.findLineById(1L);

        //then
        assertThat(line.getName()).isEqualTo(name);
        assertThat(line.getColor()).isEqualTo(color);
        assertThat(line.getExtraFare()).isEqualTo(extraFare);
    }

    @Test
    @DisplayName("구간 추가")
    void addStationOnLine() {
        //given
        Long lineId = 1L;
        Long upStationId = 3L;
        Long downStationId = 4L;
        int distance = 10;
        stationService.saveStation(new StationRequest("회기역"));
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);

        //when
        lineService.addLineStation(lineId, sectionRequest);
        Line line = lineService.findLineById(lineId);

        //then
        assertThat(line.getStations().size()).isEqualTo(4);

    }

    @Test
    @DisplayName("구간 삭제")
    void deleteStationOnLine() {
        //given
        Long lineId = 1L;
        Long stationId = 3L;

        //when
        lineService.removeLineStation(lineId, stationId);
        Line line = lineService.findLineById(lineId);

        //then
        assertThat(line.getStations().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("전 노선 조회")
    void entireLine() {
        //when
        List<Line> lines = lineService.findLines();

        //then
        assertThat(lines.size()).isEqualTo(1);
    }
}