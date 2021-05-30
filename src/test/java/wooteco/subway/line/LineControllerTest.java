package wooteco.subway.line;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.ui.LoginInterceptor;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.*;
import wooteco.subway.line.ui.LineController;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = LineController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginInterceptor loginInterceptor;

    @MockBean
    private AuthService authService;

    @MockBean
    private LineService lineService;

    @Test
    @DisplayName("노선 생성")
    public void createLine() throws Exception {
        //given & when
        LineRequest lineRequest = new LineRequest("2호선", "초록색", 1L, 2L, 100);
        StationResponse stationResponse = new StationResponse(1L, "당산역");
        StationResponse stationResponse2 = new StationResponse(2L, "합정역");

        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(lineService.saveLine(any(LineRequest.class)))
                .willReturn(new LineResponse(1L, "2호선", "초록색", Arrays.asList(stationResponse, stationResponse2)));

        //then
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
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        Station startStation = new Station("1호선 시작역");
        Station endStation = new Station("1호선 끝역");
        Station startStation2 = new Station("2호선 시작역");
        Station endStation2 = new Station("2호선 끝역");
        TotalLineResponse totalLineResponse = new TotalLineResponse(1L, "1호선", "파랑색", startStation, endStation, 215);
        TotalLineResponse totalLineResponse2 = new TotalLineResponse(1L, "2호선", "초록색", startStation2, endStation2, 315);
        given(lineService.findTotalLineResponses()).willReturn(Arrays.asList(totalLineResponse, totalLineResponse2));

        //then
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
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        StationResponse stationResponse = new StationResponse(1L, "당산역");
        StationResponse stationResponse2 = new StationResponse(2L, "합정역");

        given(lineService.findLineResponseById(1L))
                .willReturn(new LineResponse(1L, "2호선", "초록색", Arrays.asList(stationResponse, stationResponse2)));

        //then
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
        LineRequest lineRequest = new LineRequest("2호선", "초록색", 1L, 2L, 100);
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        //then
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
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        //then
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
        //given & when
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 100);

        //then
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
        //given & when
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 100);

        //then
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
        //given & when
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        TransferLineResponse transferLineResponse1 = new TransferLineResponse(1L, "2호선", "초록색");
        TransferLineResponse transferLineResponse2 = new TransferLineResponse(2L, "9호선", "황토색");

        SectionResponse sectionResponse1 = new SectionResponse(1L, "신목동역", 10, Collections.emptyList());
        SectionResponse sectionResponse2 = new SectionResponse(2L, "당산", 0, Collections.singletonList(transferLineResponse1));
        SectionResponse sectionResponse3 = new SectionResponse(3L, "당산", 10, Collections.singletonList(transferLineResponse2));
        SectionResponse sectionResponse4 = new SectionResponse(4L, "합정역", 0, Collections.emptyList());

        LineMapResponse lineMapResponse1 = new LineMapResponse(1L, "9호선", "황토색", 10, Arrays.asList(sectionResponse1, sectionResponse2));
        LineMapResponse lineMapResponse2 = new LineMapResponse(2L, "2호선", "초록색", 10, Arrays.asList(sectionResponse3, sectionResponse4));


        given(lineService.findLineMapResponses()).willReturn(Arrays.asList(lineMapResponse1, lineMapResponse2));

        //then
        mockMvc.perform(get("/api/lines/map")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("findLineMap",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
