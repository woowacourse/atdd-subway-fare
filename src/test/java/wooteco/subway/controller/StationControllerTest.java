package wooteco.subway.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("역 관련 테스트")
@WebMvcTest(controllers = StationController.class)
public class StationControllerTest extends ControllerTest {
    public static final StationRequest 잠실역 = new StationRequest("잠실역");
    public static final StationResponse RESPONSE = new StationResponse(1L, "잠실역");
    public static final FieldDescriptor[] FIELD_DESCRIPTORS = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")};
    public static final List<StationResponse> STATION_RESPONSES = Arrays.asList(
            new StationResponse(1L, "잠실역"),
            new StationResponse(2L, "삼전역"),
            new StationResponse(3L, "강남역"),
            new StationResponse(4L, "신천역"),
            new StationResponse(5L, "마찌역")
    );
    @MockBean
    private StationService stationService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("생성 - 성공")
    public void createStation() throws Exception {
        given(stationService.saveStation(any(StationRequest.class))).willReturn(RESPONSE);

        mockMvc.perform(post("/stations")
                .content(objectMapper.writeValueAsString(잠실역))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(RESPONSE)))
                .andDo(print())
                .andDo(document("station-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")
                        )
                ));
    }

    @Test
    @DisplayName("조회 - 성공")
    public void showStations() throws Exception {
        //given
        given(stationService.findAllStationResponses()).willReturn(STATION_RESPONSES);

        mockMvc.perform(get("/stations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*.name").value(Matchers
                        .containsInAnyOrder("잠실역", "삼전역", "강남역", "신천역", "마찌역")))
                .andDo(print())
                .andDo(document("station-find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").description("역 목록"))
                                .andWithPrefix("[].", FIELD_DESCRIPTORS)));
    }

    @Test
    @DisplayName("삭제 - 성공")
    public void deleteStation() throws Exception {
        willDoNothing().given(stationService).deleteStationById(1L);

        mockMvc.perform(delete("/stations/1"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("station-delete",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}
