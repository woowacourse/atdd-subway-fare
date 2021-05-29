package wooteco.subway.station;

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
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.ui.StationController;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StationController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginInterceptor loginInterceptor;

    @MockBean
    private AuthService authService;

    @MockBean
    private StationService stationService;

    @Test
    @DisplayName("역 생성")
    public void createStation() throws Exception {
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(stationService.saveStation(any(StationRequest.class)))
                .willReturn(new StationResponse(1L, "가양역"));
        StationRequest stationRequest = new StationRequest("가양역");

        //then
        mockMvc.perform(post("/api/stations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(stationRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("createStation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("역 조회")
    public void showStations() throws Exception {
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        StationResponse stationResponse = new StationResponse(1L, "가양역");
        StationResponse stationResponse2 = new StationResponse(2L, "등촌역");

        given(stationService.findAllStationResponses())
                .willReturn(Arrays.asList(stationResponse, stationResponse2));

        //then
        mockMvc.perform(get("/api/stations")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("showStations",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("역 조회")
    public void deleteStation() throws Exception {
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        //then
        mockMvc.perform(delete("/api/stations/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("deleteStation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
