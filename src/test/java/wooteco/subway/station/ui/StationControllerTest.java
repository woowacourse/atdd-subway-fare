package wooteco.subway.station.ui;

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
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StationController.class)
@ActiveProfiles("test1")
@AutoConfigureRestDocs
class StationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StationService stationService;
//    @MockBean
//    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("역 생성 - 성공")
    public void createStation() throws Exception {
        //given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        given(stationService.saveStation(any(StationRequest.class)))
            .willReturn(stationResponse);
        mockMvc.perform(post("/stations")
            .content(objectMapper.writeValueAsString(stationRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("name").value(stationRequest.getName()))
            .andDo(print())
            .andDo(document("station-create"));
    }

    @Test
    @DisplayName("역 조회 성공")
    public void showStations() throws Exception {
        //given
        List<StationResponse> stationResponses = Arrays.asList(
            new StationResponse(1L, "잠실역"),
            new StationResponse(2L, "강남역"),
            new StationResponse(3L, "사당역"),
            new StationResponse(4L, "잠실새내역"),
            new StationResponse(5L, "종합운동장역")
        );

        given(stationService.findAllStationResponses())
            .willReturn(stationResponses);

        mockMvc.perform(get("/stations")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*").isArray())
            .andExpect(jsonPath("$.*.name")
                .value(Matchers.containsInAnyOrder("잠실역", "강남역", "사당역", "잠실새내역", "종합운동장역")))
            .andDo(print())
            .andDo(document("station-find"));
    }

    @Test
    @DisplayName("역 삭제 성공")
    public void deleteStation() throws Exception {
        mockMvc.perform(delete("/stations/1")
        )
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("station-delete"));
    }
}