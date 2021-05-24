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
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.exception.DuplicatedStationNameException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@WebMvcTest(controllers = StationController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class StationControllerTest {

    @MockBean
    private StationService stationService;
    @MockBean
    private AuthService authService;

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
        given(stationService.findAllStationResponses()).willReturn(stationResponses);

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

    @DisplayName("역 삭제 - 성공")
    @Test
    public void deleteStation() throws Exception {
        mockMvc.perform(delete("/api/stations/1"))
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
