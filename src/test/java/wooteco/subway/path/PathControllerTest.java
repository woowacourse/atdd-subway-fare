package wooteco.subway.path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
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
import wooteco.subway.auth.ui.AuthenticationInterceptor;
import wooteco.subway.member.domain.Member;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.ui.PathController;
import wooteco.subway.station.dto.StationResponse;

@WebMvcTest(controllers = PathController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    private AuthService authService;

    @MockBean
    private PathService pathService;

    @Test
    @DisplayName("경로 조회")
    public void findPath() throws Exception {
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(authService.findMemberByToken(null))
            .willReturn(new Member(1L, "email@email.com", "password", 20));
        StationResponse stationResponse = new StationResponse(1L, "건대입구역");
        StationResponse stationResponse2 = new StationResponse(2L, "용마산역");
        given(pathService.findPath(new Member(1L, "email@email.com", "password", 20), 1L, 2L))
            .willReturn(new PathResponse(Arrays.asList(stationResponse, stationResponse2), 20, 1250));
        //then
        mockMvc.perform(get("/api/paths")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", "1")
            .param("target", "2"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("findPath",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
}
