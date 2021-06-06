package wooteco.subway.path.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.ui.dto.PathResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.ui.dto.StationResponse;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.docs.ApiDocumentUtils.getDocumentRequest;
import static wooteco.docs.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(controllers = PathController.class)
@AutoConfigureRestDocs
class PathControllerTest {
    private static final StationResponse 강남역Response = new StationResponse(1L, "강남역");
    private static final StationResponse 선릉역Response = new StationResponse(1L, "선릉역");
    private static final StationResponse 역삼역Response = new StationResponse(1L, "역삼역");
    private static final StationResponse 잠실역Response = new StationResponse(1L, "잠실역");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;

    @MockBean
    private AuthService authService;

    @Test
    void findPath_inLoginCase() throws Exception {
        given(pathService.findPath(anyLong(), anyLong(), any(LoginMember.class)))
                .willReturn(new PathResponse(
                        Arrays.asList(
                                강남역Response,
                                선릉역Response,
                                역삼역Response,
                                잠실역Response
                        ), 10, 1000
                ));

        ResultActions perform = mockMvc.perform(
                get("/paths?source={sourceStationId}&target={targetStationId}", 1, 2)
                .header(AUTHORIZATION, "bearer {TOKEN}")
        ).andExpect(status().isOk());

        perform.andDo(document("path - find : in login case",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("source").description("출발 역 id"),
                        parameterWithName("target").description("도착 역 id")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION).description("Bearer token")
                )
        ));
    }

    @Test
    void findPath_inGuestCase() throws Exception {
        given(pathService.findPath(anyLong(), anyLong(), any(LoginMember.class)))
                .willReturn(new PathResponse(
                        Arrays.asList(
                                강남역Response,
                                선릉역Response,
                                역삼역Response,
                                잠실역Response
                        ), 10, 1000
                ));

        ResultActions perform = mockMvc.perform(
                get("/paths?source={sourceStationId}&target={targetStationId}", 1, 2)
        ).andExpect(status().isOk());

        perform.andDo(document("path - find : in guest case",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("source").description("출발 역 id"),
                        parameterWithName("target").description("도착 역 id")
                )
        ));
    }
}