package wooteco.subway.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.ui.PathController;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("경로 관련 테스트")
@WebMvcTest(controllers = PathController.class)
public class PathControllerTest extends ControllerTest {
    @MockBean
    PathService pathService;

    @MockBean
    AuthService authService;

    @Test
    @DisplayName("조회 - 성공")
    public void showPath() throws Exception {
        //given
        long source = 1L;
        long target = 2L;
        Station station1 = new Station("테스트1");
        Station station2 = new Station("테스트2");
        Station station3 = new Station("테스트3");
        PathResponse pathResponse = new PathResponse(StationResponse.listOf(Arrays.asList(station1, station2, station3)),
                30, 2800);
        LoginMember loginMember = new LoginMember(1L, "email@eamil.com", 12);
        given(authService.findMemberByToken("secrettokentoken")).willReturn(loginMember);
        given(pathService.findPath(eq(loginMember.getAge()), eq(1L), eq(2L))).willReturn(pathResponse);

        mockMvc.perform(get("/paths?source=" + source + "&target=" + target)
                .header(HttpHeaders.AUTHORIZATION, "Bearer secrettokentoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations[*].name").value(
                        Matchers.containsInRelativeOrder("테스트1", "테스트2", "테스트3")))
                .andDo(document("path-find"));
    }
}
