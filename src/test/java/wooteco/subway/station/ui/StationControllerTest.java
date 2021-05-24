package wooteco.subway.station.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import wooteco.subway.ExceptionAdviceController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

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
    private AuthService authService;

    @Test
    @DisplayName("역 생성 - 성공")
    public void createStation() throws Exception {
        //given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        given(stationService.saveStation(any(StationRequest.class)))
                .willReturn(stationResponse);

        mockMvc.perform(post("/api/stations")
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
        final List<StationResponse> stationResponses = Arrays.asList(
                new StationResponse(1L, "잠실역"),
                new StationResponse(2L, "강남역"),
                new StationResponse(3L, "사당역"),
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
        given(stationService.updateStation(any(Long.class), any(StationRequest.class)))
                .willReturn(stationResponse);

        mockMvc.perform(put("/api/stations/1")
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
        given(stationService.updateStation(any(), any()))
                .willThrow(new DuplicateNameException());

        final StationRequest stationRequest = new StationRequest("newName");
        mockMvc.perform(put("/api/stations/1")
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
        mockMvc.perform(
                delete("/api/stations/1")
        )
                .andExpect(status().isNoContent())
                .andDo(document("station-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }


}
