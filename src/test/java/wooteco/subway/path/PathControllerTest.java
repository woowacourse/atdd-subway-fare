package wooteco.subway.path;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.ui.LoginInterceptor;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.member.domain.Member;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.ui.PathController;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.ui.StationController;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PathController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class PathControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginInterceptor loginInterceptor;

    @MockBean
    private AuthService authService;

    @MockBean
    private PathService pathService;

    @Test
    @DisplayName("경로 조회")
    public void findPath() throws Exception {
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(authService.findMemberByToken(null))
                .willReturn(new Member(1L, "email@email.com", "secretPassword", 33));
        StationResponse stationResponse = new StationResponse(1L, "가양역");
        StationResponse stationResponse2 = new StationResponse(2L, "증미역");
        given(pathService.findPath(1L, 2L, 33))
                .willReturn(new PathResponse(Arrays.asList(stationResponse, stationResponse2), 20, 1450));
        //then
        mockMvc.perform(get("/api/paths")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", "1")
                .param("target", "2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("findPath"));
    }
}
