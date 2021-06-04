package wooteco.subway.station.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.infrastructure.LoginInterceptor;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.duplicate.DuplicatedStationNameException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.SimpleLineResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@WebMvcTest(controllers = StationController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class StationControllerTest {

    @MockBean
    private StationService stationService;
    @MockBean
    private LoginInterceptor loginInterceptor;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("역 생성 테스트")
    @Test
    public void createStation() throws Exception {
        //given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        given(stationService.saveStation(any(StationRequest.class))).willReturn(stationResponse);
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        //when
        mockMvc.perform(post("/api/stations")
            .content(objectMapper.writeValueAsString(stationRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            // then
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("name").value(stationResponse.getName()))
            .andDo(print())
            .andDo(document("station-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

    @DisplayName("역 생성 테스트 - 실패(인증)")
    @Test
    public void createStationFail() throws Exception {
        //given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");

        given(stationService.saveStation(any(StationRequest.class))).willReturn(stationResponse);
        given(loginInterceptor.preHandle(any(), any(), any()))
            .willThrow(AuthorizationException.class);

        //when
        mockMvc.perform(post("/api/stations")
            .content(objectMapper.writeValueAsString(stationRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            // then
            .andExpect(status().isUnauthorized())
            .andDo(print())
            .andDo(document("station-create-fail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

    @DisplayName("역 조회 - 성공")
    @Test
    public void showStations() throws Exception {
        //given
        List<StationResponse> stationResponses = Arrays.asList(
            new StationResponse(1L, "잠실역"),
            new StationResponse(2L, "잠실새내역"),
            new StationResponse(3L, "종합운동장역"),
            new StationResponse(4L, "삼성역"),
            new StationResponse(5L, "선릉역")
        );
        given(stationService.findAllStations()).willReturn(stationResponses);
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        //when
        mockMvc.perform(get("/api/stations"))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*").isArray())
            .andExpect(jsonPath("$.*.name")
                .value(Matchers.containsInAnyOrder("잠실역", "잠실새내역", "종합운동장역", "삼성역", "선릉역"))
            )
            .andDo(print())
            .andDo(document("station-show",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

    @DisplayName("역 조회 - 성공(노선 정보 포함)")
    @Test
    public void showStationsWithLine() throws Exception {
        //given
        List<StationResponse> stationResponses = Arrays.asList(
            StationResponse.of(
                new Station(1L, "잠실역"),
                SimpleLineResponse.listOf(
                    Arrays.asList(
                        new Line(1L, "2호선", "be-green-200"),
                        new Line(2L, "8호선", "be-pink-200")
                    )
                )
            ),
            StationResponse.of(
                new Station(2L, "강남역"),
                SimpleLineResponse.listOf(
                    Arrays.asList(new Line(1L, "2호선", "be-green-200"))
                )
            )
        );
        given(stationService.findAllStations()).willReturn(stationResponses);
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        //when
        mockMvc.perform(get("/api/stations"))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*").isArray())
            .andExpect(jsonPath("$.*.name")
                .value(Matchers.containsInAnyOrder("잠실역", "강남역"))
            )
            .andExpect(jsonPath("$.*.lines[*].name")
                .value(Matchers.containsInAnyOrder("2호선", "8호선", "2호선")))
            .andDo(print())
            .andDo(document("station-show-line",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

    @DisplayName("역 삭제 - 성공")
    @Test
    public void deleteStation() throws Exception {
        // given
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        // when
        mockMvc.perform(delete("/api/stations/1"))
            // then
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("station-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

    @DisplayName("역 수정 - 성공")
    @Test
    public void updateStationSuccess() throws Exception {
        // given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        given(stationService.updateStation(any(Long.class), any(StationRequest.class)))
            .willReturn(stationResponse);
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        //when
        mockMvc.perform(put("/api/stations/1")
            .content(objectMapper.writeValueAsString(stationRequest))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(stationResponse.getId()))
            .andExpect(jsonPath("name").value(stationResponse.getName()))
            .andDo(print())
            .andDo(document("station-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }

    @DisplayName("역 수정 - 실패(중복 이름)")
    @Test
    public void updateStations() throws Exception {
        // given
        StationRequest stationRequest = new StationRequest("잠실역");
        given(stationService.updateStation(any(Long.class), any(StationRequest.class)))
            .willThrow(new DuplicatedStationNameException());
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        // when
        mockMvc.perform(put("/api/stations/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(stationRequest))
        )
            // then
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(document("station-update-fail",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
            ));
    }
}
