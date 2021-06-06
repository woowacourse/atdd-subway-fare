package wooteco.subway.line.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.auth.AuthenticationPrincipalConfig;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.line.ui.dto.*;
import wooteco.subway.line.ui.dto.sectionsofline.LineWithTransferLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.SectionsOfLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.StationOfLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.TransferLineResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.ui.dto.StationResponse;

import java.util.Collections;

import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.docs.ApiDocumentUtils.getDocumentRequest;
import static wooteco.docs.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(controllers = {LineController.class, AuthenticationPrincipalConfig.class})
@AutoConfigureRestDocs
class LineControllerTest {
    private static final Line 백기선 = new Line(
            1L,
            "백기선",
            "red",
            new Sections(Collections.singletonList(
                    new Section(
                            1L,
                            new Station(1L, "강남역"),
                            new Station(2L, "선릉역"),
                            10
                    )
            )), 1000);

    private static final Line 삼팔선 = new Line(
            2L,
            "삼팔선",
            "green",
            new Sections(Collections.singletonList(
                    new Section(
                            1L,
                            new Station(1L, "강남역"),
                            new Station(2L, "너역"),
                            10
                    )
            )), 1000);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private LineService lineService;


    @Test
    void createLine() throws Exception {
        LineResponse lineResponse = lineToLineResponse(백기선);
        given(lineService.saveLine(any(LineRequest.class))).willReturn(
                lineResponse
        );

        ResultActions perform = mockMvc.perform(post("/lines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(create백기선Request())));

        perform.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/1"))
                .andExpect(content().json(objectMapper.writeValueAsString(lineResponse)))
                .andDo(document("line - create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getLineRequestSnippet(),
                        getLineResponseSnippet()));

    }

    private ResponseFieldsSnippet getLineResponseSnippet() {
        return responseFields(
                fieldWithPath("id").type(NUMBER).description("노선 id"),
                fieldWithPath("name").type(STRING).description("노선 이름"),
                fieldWithPath("color").type(STRING).description("노선 색깔"),
                fieldWithPath("stations").type(ARRAY).description("노선에 포함된 역"),
                fieldWithPath("stations[].id").type(NUMBER).description("역 id"),
                fieldWithPath("stations[].name").type(STRING).description("역 이름")
        );
    }

    private LineRequest create백기선Request() {
        return new LineRequest(
                백기선.getName(),
                백기선.getColor(),
                1L,
                2L,
                10,
                1000
        );
    }

    @Test
    void findAllLines() throws Exception {
        LineResponse lineResponse = lineToLineResponse(백기선);
        given(lineService.findLineResponses()).willReturn(Collections.singletonList(
                lineResponse
        ));

        ResultActions perform = mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andExpect(
                        content().json(
                                objectMapper.writeValueAsString(
                                        Collections.singletonList(lineResponse)
                                )
                        ));

        perform.andDo(document("line - find all",
                getDocumentRequest(),
                getDocumentResponse(),
                getLineResponsesSnippet()
        ));
    }

    private ResponseFieldsSnippet getLineResponsesSnippet() {
        return responseFields(
                fieldWithPath("[].id").type(NUMBER).description("노선 id"),
                fieldWithPath("[].name").type(STRING).description("노선 이름"),
                fieldWithPath("[].color").type(STRING).description("노선 색깔"),
                fieldWithPath("[].stations").type(ARRAY).description("노선에 포함된 역"),
                fieldWithPath("[].stations[].id").type(NUMBER).description("역 id"),
                fieldWithPath("[].stations[].name").type(STRING).description("역 이름")
        );
    }

    @Test
    void findLineById() throws Exception {
        LineResponse lineResponse = lineToLineResponse(백기선);
        given(lineService.findLineResponseById(anyLong())).willReturn(lineResponse);

        ResultActions perform = mockMvc.perform(get("/lines/{lineId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lineResponse)));

        perform.andDo(document("line - find by id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lineId").description("노선 id")
                ),
                getLineResponseSnippet()
        ));
    }

    @Test
    void updateLine() throws Exception {
        doNothing().when(lineService).updateLine(anyLong(), any(LineRequest.class));
        ResultActions perform = mockMvc.perform(
                put("/lines/{lineId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create백기선Request()))
        ).andExpect(status().isOk());

        perform.andDo(document("line - update",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lineId").description("노선 id")
                ),
                getLineRequestSnippet()
        ));
    }

    @Test
    void deleteLiRne() throws Exception {
        doNothing().when(lineService).deleteLineById(anyLong());
        ResultActions perform = mockMvc.perform(delete("/lines/{lineId}", 1))
                .andExpect(status().isNoContent());

        perform.andDo(document("line - delete",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lineId").description("노선 id")
                )
        ));
    }

    @Test
    void getSectionsOfLine() throws Exception {
        SectionsOfLineResponse sectionsOfLineResponse = createSectionsOfLineResponse();
        given(lineService.getSectionsResponseOfLine(anyLong()))
                .willReturn(sectionsOfLineResponse);

        ResultActions perform = mockMvc.perform(get("/lines/{lineId}/sections", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sectionsOfLineResponse)));

        perform.andDo(
                document("line - find all sections",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("lineId").description("노선 id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("노선 id"),
                                fieldWithPath("color").type(STRING).description("노선 색깔"),
                                fieldWithPath("name").type(STRING).description("노선 이름"),
                                fieldWithPath("stations").type(ARRAY).description("노선에 포함된 역들"),
                                fieldWithPath("stations[].id").type(NUMBER).description("역 id"),
                                fieldWithPath("stations[].name").type(STRING).description("역 이름"),
                                fieldWithPath("stations[].transferLines").type(ARRAY).description("환승 가능 노선"),
                                fieldWithPath("stations[].transferLines[].id").type(NUMBER).description("노선 id"),
                                fieldWithPath("stations[].transferLines[].name").type(STRING).description("노선 이름"),
                                fieldWithPath("stations[].transferLines[].color").type(STRING).description("노선 색깔"),
                                fieldWithPath("sections").type(ARRAY).description("노선의 구간들"),
                                fieldWithPath("sections[].upStation").type(OBJECT).description("상행 역"),
                                fieldWithPath("sections[].upStation.id").type(NUMBER).description("역 id"),
                                fieldWithPath("sections[].upStation.name").type(STRING).description("역 이름"),
                                fieldWithPath("sections[].downStation").type(OBJECT).description("하행 역"),
                                fieldWithPath("sections[].downStation.id").type(NUMBER).description("역 id"),
                                fieldWithPath("sections[].downStation.name").type(STRING).description("역 이름"),
                                fieldWithPath("sections[].distance").type(NUMBER).description("구간의 거리")
                        )
                )
        );
    }

    private SectionsOfLineResponse createSectionsOfLineResponse() {
        return new SectionsOfLineResponse(
                new LineWithTransferLineResponse(
                        백기선.getId(),
                        백기선.getColor(),
                        백기선.getName(),
                        Collections.singletonList(
                                new StationOfLineResponse(
                                        1L,
                                        "강남역",
                                        Collections.singletonList(
                                                new TransferLineResponse(삼팔선)
                                        )
                                )
                        )
                ),
                new SectionsResponse(
                        Collections.singletonList(
                                new SectionResponse(
                                        new Section(1L, new Station(1L, "강남역"), new Station(2L, "선릉역"), 10)
                                )
                        )
                )
        );
    }

    @Test
    void addLineStation() throws Exception {
        doNothing().when(lineService).addLineStation(anyLong(), any(SectionRequest.class));

        ResultActions perform = mockMvc.perform(
                post("/lines/{lineId}/sections", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        new SectionRequest(1L, 2L, 10)
                                )
                        ))
                .andExpect(status().isOk());

        perform.andDo(document("section - create",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lineId").description("노선 id")
                )
        ));
    }

    @Test
    void removeLineStation() throws Exception {
        doNothing().when(lineService).removeLineStation(anyLong(), anyLong());

        ResultActions perform = mockMvc.perform(
                delete("/lines/{lineId}/sections?stationId={stationId}", 1, 1)
        ).andExpect(status().isOk());

        perform.andDo(document("section - delete",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lineId").description("노선 id")
                ),
                requestParameters(
                        parameterWithName("stationId").description("역 id")
                )
        ));
    }

    @Test
    void updateSection() throws Exception {
        doNothing().when(lineService).updateSectionDistance(anyLong(), anyLong(), anyLong(), anyInt());

        ResultActions perform = mockMvc.perform(
                put("/lines/{lineId}/sections?upStationId={upStationId}&downStationId={downStationId}", 1, 1, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SectionDistanceRequest(10)))
        ).andExpect(status().isOk());

        perform.andDo(document("section - update",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("lineId").description("노선 id")
                ),
                requestParameters(
                        parameterWithName("upStationId").description("상행 역 id"),
                        parameterWithName("downStationId").description("하행 역 id")
                )
        ));
    }

    private RequestFieldsSnippet getLineRequestSnippet() {
        return requestFields(
                fieldWithPath("name").type(STRING).description("노선 이름"),
                fieldWithPath("color").type(STRING).description("노선 색깔"),
                fieldWithPath("upStationId").type(NUMBER).description("상행 역 id"),
                fieldWithPath("downStationId").type(NUMBER).description("하행 역 id"),
                fieldWithPath("distance").type(NUMBER).description("상행역과 하행역 사이의 거리"),
                fieldWithPath("extraFare").type(NUMBER).description("추가 요금")
        );
    }

    private LineResponse lineToLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations().stream()
                        .map(station ->
                                new StationResponse(
                                        station.getId(),
                                        station.getName()
                                )
                        ).collect(toList()));
    }
}