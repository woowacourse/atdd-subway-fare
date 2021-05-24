package wooteco.subway.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.ui.StationController;

@DisplayName("역 관련 테스트")
@WebMvcTest(controllers = StationController.class)
public class StationControllerTest extends ControllerTest {
    @MockBean
    StationService stationService;
}
