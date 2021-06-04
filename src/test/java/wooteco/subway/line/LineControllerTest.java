package wooteco.subway.line;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
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
import wooteco.subway.auth.ui.AuthenticationInterceptor;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.CustomLineResponse;
import wooteco.subway.line.dto.LineMapResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.line.ui.LineController;
import wooteco.subway.section.dto.SectionResponse;
import wooteco.subway.station.dto.StationResponse;


@WebMvcTest(controllers = LineController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    private AuthService authService;

    @MockBean
    private LineService lineService;

    @Test
    @DisplayName("노선 생성")
    public void createLine() throws Exception {
        // given
        LineRequest lineRequest = new LineRequest("2호선", "bg-green-100", 1L, 2L, 100, 350);
        StationResponse stationResponse = new StationResponse(1L, "건대입구역");
        StationResponse stationResponse2 = new StationResponse(2L, "성수역");

        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(lineService.createLine(any(LineRequest.class)))
            .willReturn(new LineResponse(1L, "2호선", "bg-green-100", 350, Arrays.asList(stationResponse, stationResponse2)));

        // then
        mockMvc.perform(post("/api/lines")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(lineRequest)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("createLine",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("노선 전체 조회")
    public void findAllLines() throws Exception {
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        StationResponse startStation1 = new StationResponse(1L, "1호선 시작역");
        StationResponse endStation1 = new StationResponse(2L, "1호선 마지막역");
        StationResponse startStation2 = new StationResponse(3L, "2호선 시작역");
        StationResponse endStation2 = new StationResponse(4L, "2호선 마지막역");
        CustomLineResponse totalLineResponse = new CustomLineResponse(1L, "1호선", "bg-blue-100", startStation1, endStation1, 215);
        CustomLineResponse totalLineResponse2 = new CustomLineResponse(1L, "2호선", "bg-green-100", startStation2, endStation2, 315);
        given(lineService.findCustomLineResponses()).willReturn(Arrays.asList(totalLineResponse, totalLineResponse2));

        // then
        mockMvc.perform(get("/api/lines")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("findAllLines",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("Id로 노선 조회")
    public void findLineById() throws Exception {
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        StationResponse stationResponse = new StationResponse(1L, "건대입구역");
        StationResponse stationResponse2 = new StationResponse(2L, "성수역");

        given(lineService.findOne(1L))
            .willReturn(new LineResponse(1L, "2호선", "bg-green-100", 0, Arrays.asList(stationResponse, stationResponse2)));

        // then
        mockMvc.perform(get("/api/lines/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("findLineById",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("노선 수정")
    public void updateLine() throws Exception {
        LineRequest lineRequest = new LineRequest("2호선", "bg-green-100", 1L, 2L, 100, 0);
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        // then
        mockMvc.perform(put("/api/lines/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(lineRequest)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("updateLine",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("노선 삭제")
    public void deleteLine() throws Exception {
        // given
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        // then
        mockMvc.perform(delete("/api/lines/1")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("deleteLine",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("구간 추가")
    public void addLineStation() throws Exception {
        // given
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 100);

        // then
        mockMvc.perform(post("/api/lines/1/sections")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(sectionRequest)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("addLineStation",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("구간 삭제")
    public void removeLineStation() throws Exception {
        // given
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 100);

        // then
        mockMvc.perform(delete("/api/lines/1/sections")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("stationId", "1"))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("removeLineStation",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("신규 전체 조회")
    public void findLineMap() throws Exception {
        // given
        given(authenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        TransferLineResponse transferLineResponse1 = new TransferLineResponse(2L, "2호선", "bg-green-100");
        TransferLineResponse transferLineResponse2 = new TransferLineResponse(7L, "7호선", "bg-green-200");

        SectionResponse sectionResponse1 = new SectionResponse(1L, "면목역", 10, Collections.emptyList());
        SectionResponse sectionResponse2 = new SectionResponse(2L, "용마산역", 0, Collections.singletonList(transferLineResponse1));
        SectionResponse sectionResponse3 = new SectionResponse(3L, "건대입구역", 10, Collections.singletonList(transferLineResponse2));
        SectionResponse sectionResponse4 = new SectionResponse(4L, "성수역", 0, Collections.emptyList());

        LineMapResponse lineMapResponse1 = new LineMapResponse(2L, "2호선", "bg-green-100", 10, 0, Arrays.asList(sectionResponse3, sectionResponse4));
        LineMapResponse lineMapResponse2 = new LineMapResponse(7L, "7호선", "bg-green-200", 10, 500, Arrays.asList(sectionResponse1, sectionResponse2));

        given(lineService.findLineMapResponses()).willReturn(Arrays.asList(lineMapResponse1, lineMapResponse2));

        // then
        mockMvc.perform(get("/api/lines/map")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("findLineMap",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
}