package wooteco.subway.line.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.TestDataLoader;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LineController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class LineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("노선 생성 - 성공")
    public void createStation() throws Exception {
        //given
        final LineRequest lineRequest = new LineRequest("2호선", "bg-green-200", 1L, 2L, 5);
        final List<StationResponse> stationResponses = Arrays.asList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "잠실역")
        );
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "bg-green-200", stationResponses);
        given(lineService.saveLine(any(LineRequest.class)))
                .willReturn(lineResponse);

        mockMvc.perform(post("/api/lines")
                .content(objectMapper.writeValueAsString(lineRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("name").value(lineResponse.getName()))
                .andExpect(jsonPath("color").value(lineResponse.getColor()))
                .andExpect(jsonPath("stations[*].name")
                        .value(containsInAnyOrder("강남역", "잠실역")))
                .andDo(print())
                .andDo(document("line-create"));
    }

    @Test
    @DisplayName("노선 전체 조회 - 성공")
    public void showLines() throws Exception{
        final TestDataLoader testDataLoader = new TestDataLoader();
        final List<LineResponse> lineResponses = LineResponse
                .listOf(Arrays.asList(testDataLoader.신분당선(), testDataLoader.이호선()));
        given(lineService.findLineResponses()).willReturn(lineResponses);

        mockMvc.perform(get("/api/lines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("신분당선", "2호선")))
                .andExpect(jsonPath("$[*].stations[*].name")
                        .value(containsInAnyOrder("강남역", "판교역", "정자역", "강남역", "역삼역", "잠실역")))
                .andDo(print())
                .andDo(document("line-find"));
    }

    @Test
    @DisplayName("노선 ID 조회 - 성공")
    public void showLineById() throws Exception{
        final TestDataLoader testDataLoader = new TestDataLoader();
        final LineResponse lineResponse = LineResponse.of(testDataLoader.신분당선());
        Long id = testDataLoader.신분당선().getId();
        given(lineService.findLineResponseById(id)).willReturn(lineResponse);

        mockMvc.perform(get("/api/lines/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("신분당선"))
                .andExpect(jsonPath("stations[*].name")
                        .value(containsInAnyOrder("강남역", "판교역", "정자역")))
                .andDo(print())
                .andDo(document("line-findbyid"));
    }

    @Test
    @DisplayName("노선 수정 - 성공")
    public void updateLines() throws Exception{
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("2호선", "bg-red-200");
        mockMvc.perform(
                put("/api/lines/1")
                        .content(objectMapper.writeValueAsString(lineUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("lines-update"));
    }

    @Test
    @DisplayName("노선 삭제 - 성공")
    public void deleteLine() throws Exception {
        mockMvc.perform(
                delete("/api/lines/1")
        )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("line-delete"));
    }

    @Test
    @DisplayName("구간 추가 - 성공")
    public void createSection() throws Exception {
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 5);
        mockMvc.perform(post("/api/lines/1/sections")
                .content(objectMapper.writeValueAsBytes(sectionRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("section-create"));
    }

    @DisplayName("구간 삭제 - 성공")
    @Test
    public void removeLineStation() throws Exception {
        mockMvc.perform(delete("/api/lines/1/sections?stationId=1"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("section-delete"));
    }
}