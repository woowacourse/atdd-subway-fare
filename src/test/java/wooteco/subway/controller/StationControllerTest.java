package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import wooteco.auth.infrastructure.JwtTokenProvider;
import wooteco.common.ExceptionAdviceController;
import wooteco.common.exception.badrequest.DuplicateStationNameException;
import wooteco.subway.service.StationService;
import wooteco.subway.web.StationController;
import wooteco.subway.web.dto.request.StationRequest;
import wooteco.subway.web.dto.response.SimpleLineResponse;
import wooteco.subway.web.dto.response.StationResponse;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {StationController.class, ExceptionAdviceController.class})
@ActiveProfiles("test")
@AutoConfigureRestDocs
class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("역 생성 - 성공")
    public void createStation() throws Exception {
        //given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        String token = "이것은토큰입니다";

        given(jwtTokenProvider.validateToken(token))
                .willReturn(true);
        given(stationService.saveStation(any(StationRequest.class)))
                .willReturn(stationResponse);

        mockMvc.perform(post("/api/stations")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(stationRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("id").value(stationResponse.getId()))
                .andExpect(jsonPath("name").value(stationResponse.getName()))
                .andDo(print())
                .andDo(document("station-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("역 조회 - 성공")
    public void showStations() throws Exception {
        //given
        SimpleLineResponse simpleLineResponse1 = new SimpleLineResponse(1L, "2호선", "bg-green-200");
        SimpleLineResponse simpleLineResponse2 = new SimpleLineResponse(2L, "8호선", "bg-pink-200");
        final List<StationResponse> stationResponses = Arrays.asList(
                new StationResponse(1L, "잠실역", Arrays.asList(simpleLineResponse1, simpleLineResponse2)),
                new StationResponse(2L, "강남역", Arrays.asList(simpleLineResponse1)),
                new StationResponse(3L, "사당역", Arrays.asList(simpleLineResponse2)),
                new StationResponse(4L, "잠실새내역"),
                new StationResponse(5L, "종합운동장역")
        );
        given(stationService.findAllStationResponses())
                .willReturn(stationResponses);

        mockMvc.perform(
                get("/api/stations")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*.name")
                        .value(Matchers.containsInAnyOrder("잠실역", "강남역", "사당역", "잠실새내역", "종합운동장역")))
                .andDo(print())
                .andDo(document("station-find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("역 수정 - 성공")
    public void updateStation() throws Exception {
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        String token = "이것은토큰입니다";

        given(jwtTokenProvider.validateToken(token))
                .willReturn(true);
        given(stationService.updateStation(any(Long.class), any(StationRequest.class)))
                .willReturn(stationResponse);

        mockMvc.perform(put("/api/stations/1")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(stationRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(stationResponse.getId()))
                .andExpect(jsonPath("name").value(stationResponse.getName()))
                .andDo(print())
                .andDo(document("station-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("역 수정 - 실패(중복 이름)")
    public void updateStations() throws Exception {
        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token))
                .willReturn(true);
        given(stationService.updateStation(any(), any()))
                .willThrow(new DuplicateStationNameException());

        final StationRequest stationRequest = new StationRequest("새로운역");
        mockMvc.perform(put("/api/stations/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stationRequest))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("이미 존재하는 역 이름입니다."))
                .andDo(document("station-update-duplicate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("역 삭제 - 성공")
    public void deleteStations() throws Exception {
        //given
        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token))
                .willReturn(true);

        mockMvc.perform(
                delete("/api/stations/1")
                        .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isNoContent())
                .andDo(document("station-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
