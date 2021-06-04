package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.ui.LineController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("노선 관련 테스트")
@WebMvcTest(controllers = LineController.class)
public class LineControllerTest extends ControllerTest {
    public static final long LINE_ID = 1L;
    public static final LineRequest 이호선_REQUEST =
            new LineRequest("2호선", "주황색", 1L, 2L, 10, 100);
    public static final LineResponse 이호선_RESPONSE =
            new LineResponse(LINE_ID, "2호선", "주황색", Collections.emptyList(), Collections.emptyList());
    public static final LineResponse 삼호선_RESPONSE =
            new LineResponse(2L, "3호선", "주황색", Collections.emptyList(), Collections.emptyList());
    public static final FieldDescriptor[] LINE = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
            fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔"),
            fieldWithPath("stations").type(JsonFieldType.ARRAY).description("역 목록"),
            fieldWithPath("sections").type(JsonFieldType.ARRAY).description("구간 목록")};

    @MockBean
    private LineService lineService;
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("생성 - 성공")
    void create() throws Exception {
        String json = new ObjectMapper().writeValueAsString(이호선_REQUEST);

        given(lineService.saveLine(any(LineRequest.class))).willReturn(이호선_RESPONSE);

        mockMvc.perform(post("/lines")
                .content(json)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/" + LINE_ID))
                .andExpect(content().json(objectMapper.writeValueAsString(이호선_RESPONSE)))
                .andDo(print())
                .andDo(document("line-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔"),
                                fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("상행 역 ID"),
                                fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("하행 역 ID"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                                fieldWithPath("extraFare").type(JsonFieldType.NUMBER).description("추가 요금")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔"),
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("역 목록"),
                                fieldWithPath("sections").type(JsonFieldType.ARRAY).description("구간 목록")
                        )
                ));
    }

    @Test
    @DisplayName("전체 조회 - 성공")
    void findAllLines() throws Exception {

        List<LineResponse> lineResponses = Arrays.asList(이호선_RESPONSE, 삼호선_RESPONSE);

        given(lineService.findLineResponses()).willReturn(lineResponses);

        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andDo(print())
                .andDo(document("line-find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").description("노선 목록"))
                                .andWithPrefix("[].", LINE)));
    }

    @Test
    @DisplayName("id 조회 - 성공")
    void findById() throws Exception {
        given(lineService.findLineResponseById(LINE_ID)).willReturn(이호선_RESPONSE);

        mockMvc.perform(get("/lines/" + LINE_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(이호선_RESPONSE)))
                .andDo(print())
                .andDo(document("line-findbyid",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔"),
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("역 목록"),
                                fieldWithPath("sections").type(JsonFieldType.ARRAY).description("구간 목록")
                        )
                ));
    }

    @Test
    @DisplayName("삭제 - 성공")
    void deleteLine() throws Exception {
        mockMvc.perform(delete("/lines/" + LINE_ID))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("line-delete",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }

    @Test
    @DisplayName("수정 - 성공")
    void update() throws Exception {
        String json = new ObjectMapper().writeValueAsString(이호선_REQUEST);

        mockMvc.perform(put("/lines/" + LINE_ID)
                .content(json)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("line-update",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색깔"),
                                fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("상행 역 ID"),
                                fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("하행 역 ID"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                                fieldWithPath("extraFare").type(JsonFieldType.NUMBER).description("추가 요금")
                        )));
    }

    @Test
    @DisplayName("구간 추가 - 성공")
    void addSection() throws Exception {
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        String json = new ObjectMapper().writeValueAsString(sectionRequest);

        mockMvc.perform(post("/lines/" + LINE_ID + "/sections")
                .content(json)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("section-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("상행 역 ID"),
                                fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("하행 역 ID"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리")
                        )));
    }

    @Test
    @DisplayName("구간 삭제 - 성공")
    void deleteSection() throws Exception {
        long stationId = 1L;

        mockMvc.perform(delete("/lines/" + LINE_ID + "/sections?stationId=" + stationId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("section-delete",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}
