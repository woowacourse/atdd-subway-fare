package wooteco.subway.layer.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.acceptance.auth.application.AuthService;
import wooteco.subway.acceptance.auth.ui.AuthController;

@DisplayName("인증 관련 테스트")
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest extends ControllerTest {
    @MockBean
    AuthService authService;
}
