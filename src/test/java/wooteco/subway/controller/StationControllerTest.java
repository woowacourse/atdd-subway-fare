package wooteco.subway.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.ui.StationController;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("역 관련 테스트")
@WebMvcTest(controllers = StationController.class)
public class StationControllerTest extends ControllerTest {
    @MockBean
    private StationService stationService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("생성 - 성공")
    public void createStation() throws Exception {
        StationRequest stationRequest = new StationRequest("테스트역");
        StationResponse stationResponse = new StationResponse(1L, "테스트역");
        given(stationService.saveStation(any(StationRequest.class))).willReturn(stationResponse);

        mockMvc.perform(post("/stations")
                .content(objectMapper.writeValueAsString(stationRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(stationResponse)))
                .andDo(print())
                .andDo(document("station-create"));
    }

    @Test
    @DisplayName("삭제 - 성공")
    public void deleteStation() throws Exception {
        willDoNothing().given(stationService).deleteStationById(1L);

        mockMvc.perform(delete("/stations/1"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("station-delete"));
    }

    @Test
    @DisplayName("조회 - 성공")
    public void showStations() throws Exception {
        //given
        List<StationResponse> stationResponses = Arrays.asList(
                new StationResponse(1L, "테스트1"),
                new StationResponse(2L, "테스트2"),
                new StationResponse(3L, "테스트3"),
                new StationResponse(4L, "테스트4"),
                new StationResponse(5L, "테스트5")
        );

        given(stationService.findAllStationResponses()).willReturn(stationResponses);

        mockMvc.perform(get("/stations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*.name").value(Matchers.containsInAnyOrder("테스트1", "테스트2", "테스트3", "테스트4",
                        "테스트5")))
                .andDo(print())
                .andDo(document("station-find"));
    }
}
