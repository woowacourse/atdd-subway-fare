package wooteco.subway.layer.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.acceptance.line.application.LineService;
import wooteco.subway.acceptance.line.ui.LineController;

@WebMvcTest(controllers = LineController.class)
public class LineControllerTest extends ControllerTest {
    @MockBean
    LineService lineService;
}
