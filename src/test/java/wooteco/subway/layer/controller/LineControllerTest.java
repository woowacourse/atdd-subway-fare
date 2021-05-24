package wooteco.subway.layer.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.acceptance.line.application.LineService;
import wooteco.subway.acceptance.line.ui.LineController;

@DisplayName("노선 관련 테스트")
@WebMvcTest(controllers = LineController.class)
public class LineControllerTest extends ControllerTest {
    @MockBean
    LineService lineService;
}
