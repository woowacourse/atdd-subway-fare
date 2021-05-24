package wooteco.subway.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.ui.PathController;

@DisplayName("경로 관련 테스트")
@WebMvcTest(controllers = PathController.class)
public class PathControllerTest extends ControllerTest {
    @MockBean
    PathService pathService;
}
