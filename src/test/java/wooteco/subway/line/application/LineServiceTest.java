package wooteco.subway.line.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.exception.LineNotFoundException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    private StationResponse 테스트역1;
    private StationResponse 테스트역2;
    private StationResponse 테스트역3;

    @BeforeEach
    void setUp(){
        테스트역1 = stationService.saveStation(new StationRequest("테스트역1"));
        테스트역2 = stationService.saveStation(new StationRequest("테스트역2"));
        테스트역3 = stationService.saveStation(new StationRequest("테스트역3"));
    }

    @Test
    @DisplayName("LineRequest를 넘겨받으면, 저장 후 LineResponse를 반환한다.")
    void saveLine() {
        String 테스트선 = "테스트선";
        String 테스트색 = "테스트색";
        LineRequest lineRequest = new LineRequest(테스트선, 테스트색, 테스트역1.getId(), 테스트역2.getId(), 10);

        LineResponse lineResponse = lineService.saveLine(lineRequest);

        assertThat(lineResponse.getName()).isEqualTo(테스트선);
        assertThat(lineResponse.getColor()).isEqualTo(테스트색);
    }

    @Test
    @DisplayName("findLineResponses, findLines로 현재 라인 목록들의 LineResponse, Line들을 받아올 수 있다.")
    void findLinesAndFindLineResponses() {
        LineRequest lineRequest1 = new LineRequest("테스트선1", "테스트색1", 테스트역1.getId(), 테스트역2.getId(), 10);
        lineService.saveLine(lineRequest1);
        assertThat(lineService.findLines()).hasSize(1);
        assertThat(lineService.findLineResponses()).hasSize(1);

        LineRequest lineRequest2 = new LineRequest("테스트선2", "테스트색2", 테스트역2.getId(), 테스트역3.getId(), 10);
        lineService.saveLine(lineRequest2);
        assertThat(lineService.findLines()).hasSize(2);
        assertThat(lineService.findLineResponses()).hasSize(2);
    }

    @Test
    @DisplayName("노선 Id에 대응하는 LineResponse를 받아올 수 있다.")
    void findLineAndFindLineResponseById() {
        LineRequest lineRequest1 = new LineRequest("테스트선1", "테스트색1", 테스트역1.getId(), 테스트역2.getId(), 10);
        lineService.saveLine(lineRequest1);

        final LineResponse lineResponseById = lineService.findLineResponseById(1L);
        assertThat(lineResponseById.getName()).isEqualTo("테스트선1");
        assertThat(lineResponseById.getColor()).isEqualTo("테스트색1");

        final Line lineById = lineService.findLineById(1L);
        assertThat(lineById.getLineName()).isEqualTo("테스트선1");
        assertThat(lineById.getLineColor()).isEqualTo("테스트색1");
    }

    @Test
    @DisplayName("노선 Id에 대응하는 노선에 대해 노선 수정을 할 수 있다.")
    void updateLine() {
        LineRequest lineRequest1 = new LineRequest("테스트선1", "테스트색1", 테스트역1.getId(), 테스트역2.getId(), 10);
        lineService.saveLine(lineRequest1);

        lineService.updateLine(1L, new LineRequest("업데이트선1", "업데이트색1", null, null, 10));
        final Line lineById = lineService.findLineById(1L);
        assertThat(lineById.getLineName()).isEqualTo("업데이트선1");
        assertThat(lineById.getLineColor()).isEqualTo("업데이트색1");
    }

    @Test
    @DisplayName("노선 Id에 대응하는 노선에 대해 노선 삭제를 할 수 있다.")
    void deleteLineById() {
        LineRequest lineRequest1 = new LineRequest("테스트선1", "테스트색1", 테스트역1.getId(), 테스트역2.getId(), 10);
        lineService.saveLine(lineRequest1);

        lineService.deleteLineById(1L);

        assertThatThrownBy(() -> lineService.findLineById(1L))
                .isInstanceOf(LineNotFoundException.class);
    }

    @Test
    @DisplayName("노선 Id에 대응하는 노선에 대해 구간 추가를 할 수 있다.")
    void addLineStation() {
        LineRequest lineRequest1 = new LineRequest("테스트선1", "테스트색1", 테스트역1.getId(), 테스트역2.getId(), 10);
        lineService.saveLine(lineRequest1);
        SectionRequest sectionRequest = new SectionRequest(테스트역2.getId(), 테스트역3.getId(), 10);

        assertThatCode(() -> lineService.addLineStation(1L, sectionRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("노선 Id에 대응하는 노선에 대해 구간 삭제를 할 수 있다.")
    void removeLineStation() {
        LineRequest lineRequest1 = new LineRequest("테스트선1", "테스트색1", 테스트역1.getId(), 테스트역2.getId(), 10);
        lineService.saveLine(lineRequest1);
        SectionRequest sectionRequest = new SectionRequest(테스트역2.getId(), 테스트역3.getId(), 10);
        lineService.addLineStation(1L, sectionRequest);

        assertThatCode(() -> lineService.removeLineStation(1L, 테스트역1.getId()))
                .doesNotThrowAnyException();
    }
}