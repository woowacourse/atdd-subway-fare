package wooteco.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class LineServiceTest {

    @Autowired
    private StationDao stationDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private LineService lineService;

    @DisplayName("노선 생성 성공")
    @Test
    void saveLineSuccess() {
        // given
        Station 잠실역 = stationDao.insert(new Station("잠실역"));
        Station 신림역 = stationDao.insert(new Station("신림역"));
        String color = "cobalt-blue";
        String name = "2호선";
        int distance = 10;
        LineRequest lineRequest = new LineRequest(name, color, 잠실역.getId(), 신림역.getId(), 0, distance);

        // when
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then
        assertThat(lineResponse.getId()).isNotNull();
        assertThat(lineResponse.getColor()).isEqualTo(color);
        assertThat(lineResponse.getName()).isEqualTo(name);
        assertThat(lineResponse.getStations()).hasSize(2);
    }

    @DisplayName("노선 응답 조회 성공")
    @Test
    void findLineResponsesSuccess() {
        // given
        LineResponse lineResponse = 라인을_생성한다();

        // when
        List<LineResponse> lineResponses = lineService.findLineResponses();

        // then
        assertThat(lineResponses).hasSize(1);
        assertThat(lineResponses.get(0).getId()).isEqualTo(lineResponse.getId());
        assertThat(lineResponses.get(0).getColor()).isEqualTo(lineResponse.getColor());
        assertThat(lineResponses.get(0).getName()).isEqualTo(lineResponse.getName());
        assertThat(lineResponses.get(0).getStations()).hasSize(2);
    }

    private LineResponse 라인을_생성한다() {
        Station 잠실역 = stationDao.insert(new Station("잠실역"));
        Station 신림역 = stationDao.insert(new Station("신림역"));
        String color = "cobalt-blue";
        String name = "2호선";
        int distance = 10;

        LineRequest lineRequest = new LineRequest(name, color, 잠실역.getId(), 신림역.getId(), 0, distance);
        Line line = lineDao.insert(new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare()));

        Section section = new Section(잠실역, 신림역, lineRequest.getDistance());
        Section persistSection = sectionDao.insert(line, section);

        line.addSection(persistSection);
        return LineResponse.of(line);
    }
}