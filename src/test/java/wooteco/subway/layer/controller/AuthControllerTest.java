package wooteco.subway.layer.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.acceptance.auth.application.AuthService;
import wooteco.subway.acceptance.auth.ui.AuthController;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest extends ControllerTest {
    @MockBean
    AuthService authService;
}
