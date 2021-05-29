package wooteco.subway.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import wooteco.auth.util.JwtTokenProvider;
import wooteco.common.ExceptionAdviceController;
import wooteco.common.exception.badrequest.StationNameDuplicateException;
import wooteco.subway.service.StationService;
import wooteco.subway.web.api.StationController;
import wooteco.subway.web.dto.request.StationRequest;
import wooteco.subway.web.dto.response.SimpleLineResponse;
import wooteco.subway.web.dto.response.StationResponse;

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
    public void createStations() throws Exception {
        //given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        given(stationService.saveStation(any(StationRequest.class)))
            .willReturn(stationResponse);
        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);

        mockMvc.perform(post("/api/stations")
            .header("Authorization", "Bearer " + token)
            .content(objectMapper.writeValueAsString(stationRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("name").value(stationRequest.getName()))
            .andDo(print())
            .andDo(document("station-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("역 조회 - 성공")
    public void showStations() throws Exception {
        //given
        final SimpleLineResponse 이호선 = new SimpleLineResponse(1L, "2호선", "빨강");
        final SimpleLineResponse 팔호선 = new SimpleLineResponse(2L, "8호선", "핑크");

        final List<StationResponse> stationResponses = Arrays.asList(
            new StationResponse(1L, "잠실역", Arrays.asList(이호선, 팔호선)),
            new StationResponse(2L, "강남역", Arrays.asList(이호선)),
            new StationResponse(3L, "사당역"),
            new StationResponse(4L, "잠실새내역", Arrays.asList(이호선)),
            new StationResponse(5L, "종합운동장역", Arrays.asList(이호선))
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
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("역 삭제 - 성공")
    public void deleteStations() throws Exception {

        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);

        mockMvc.perform(
            delete("/api/stations/1")
                .header("Authorization", "Bearer " + token)
        )
            .andExpect(status().isNoContent())
            .andDo(document("station-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("역 수정 - 실패(중복 이름)")
    public void updateStations_fail() throws Exception {
        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);

        given(stationService.updateStation(any(), any()))
            .willThrow(new StationNameDuplicateException());

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
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("역 수정 - 성공")
    public void updateStations() throws Exception {
        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);
        final String newName = "새로운역";
        given(stationService.updateStation(any(), any()))
            .willReturn(new StationResponse(1L, newName));

        final StationRequest stationRequest = new StationRequest(newName);

        mockMvc.perform(put("/api/stations/1")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(stationRequest))
        )
            .andExpect(status().isOk())
            .andDo(document("station-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
}