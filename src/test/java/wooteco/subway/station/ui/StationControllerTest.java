package wooteco.subway.station.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.ui.dto.LineResponse;
import wooteco.subway.station.ui.dto.StationRequest;
import wooteco.subway.station.ui.dto.StationResponse;
import wooteco.subway.station.ui.dto.StationWithLinesResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.docs.ApiDocumentUtils.getDocumentRequest;
import static wooteco.docs.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(controllers = StationController.class)
@AutoConfigureRestDocs
class StationControllerTest {
    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 역삼역 = new Station(2L, "역삼역");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StationService stationService;

    @MockBean
    private AuthService authService;

    @Test
    void createStation() throws Exception {
        given(stationService.saveStation(any(StationRequest.class)))
                .willReturn(new StationResponse(강남역.getId(), 강남역.getName()));

        ResultActions perform = mockMvc.perform(
                post("/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationRequest(강남역.getName()))))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/stations/1"));

        perform.andDo(document("station - create",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(STRING).description("역 이름")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("역 id"),
                        fieldWithPath("name").type(STRING).description("역 이름")
                )
        ));
    }

    @Test
    void showStations() throws Exception {
        given(stationService.findAllStationResponses()).willReturn(
                Collections.singletonList(
                        new StationWithLinesResponse(
                                1L, "강남역", Collections.singletonList(
                                new LineResponse(
                                        1L,
                                        "백기선",
                                        "red"
                                ))
                        ))
        );

        ResultActions perform = mockMvc.perform(get("/stations"))
                .andExpect(status().isOk());

        perform.andDo(document("station - findAll",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[].id").type(NUMBER).description("역 id"),
                        fieldWithPath("[].name").type(STRING).description("역 이름"),
                        fieldWithPath("[].lines").type(ARRAY).description("환승 노선"),
                        fieldWithPath("[].lines[].id").type(NUMBER).description("노선 id"),
                        fieldWithPath("[].lines[].name").type(STRING).description("노선 이름"),
                        fieldWithPath("[].lines[].color").type(STRING).description("노선 색깔")
                )
        ));
    }

    @Test
    void updateStation() throws Exception {
        doNothing().when(stationService).updateStation(anyLong(), any(StationRequest.class));

        ResultActions perform = mockMvc.perform(
                put("/stations/{stationId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationRequest("강남역"))))
                .andExpect(status().isOk());

        perform.andDo(document("station - update",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("stationId").description("역 id")
                ),
                requestFields(
                        fieldWithPath("name").type(STRING).description("역 이름")
                )));
    }

    @Test
    void deleteStation() throws Exception {
        doNothing().when(stationService).deleteStationById(anyLong());

        ResultActions perform = mockMvc.perform(
                delete("/stations/{stationId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StationRequest("강남역"))))
                .andExpect(status().isNoContent());

        perform.andDo(document("station - delete",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("stationId").description("역 id")
                )));
    }
}