package wooteco.subway.layer.controller;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.acceptance.member.application.MemberService;
import wooteco.subway.acceptance.member.ui.MemberController;

@DisplayName("멤버 관련 테스트")
@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest extends ControllerTest {
    @MockBean
    MemberService memberService;
}
