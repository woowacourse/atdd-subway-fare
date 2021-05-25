package wooteco.subway.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
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
import wooteco.auth.service.AuthService;
import wooteco.subway.service.LineService;
import wooteco.subway.domain.Line;
import wooteco.subway.web.dto.request.LineRequest;
import wooteco.subway.web.dto.response.LineResponse;
import wooteco.subway.web.dto.request.LineUpdateRequest;
import wooteco.subway.web.dto.request.SectionRequest;
import wooteco.subway.web.dto.response.SectionResponse;
import wooteco.subway.web.LineController;
import wooteco.subway.web.dto.response.StationResponse;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LineController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class LineControllerTest {
    @MockBean
    private LineService lineService;

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("노선 생성 - 성공")
    @Test
    public void createLine() throws Exception {
        //given
        List<SectionResponse> sections = Arrays.asList(
                new SectionResponse(
                        1L,
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역"),
                        5
                )
        );
        LineRequest lineRequest = new LineRequest("2호선", "bg-green-200", 1L, 2L, 5);
        LineResponse lineResponse = new LineResponse(1L, "2호선", "bg-green-200",
                sections
        );
        given(lineService.saveLine(any(LineRequest.class))).willReturn(lineResponse);
        //when
        mockMvc.perform(post("/api/lines")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(lineRequest))
        )
                // then
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("name").value(lineResponse.getName()))
                .andExpect(jsonPath("color").value(lineResponse.getColor()))
                .andExpect(jsonPath("sections[*].upStation.name")
                        .value(contains("강남역")))
                .andExpect(jsonPath("sections[*].downStation.name")
                        .value(contains("역삼역")))
                .andExpect(jsonPath("sections[*].distance")
                        .value(contains(5)))
                .andDo(print())
                .andDo(document("line-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
    @DisplayName("전체 노선 조회 - 성공")
    @Test
    public void findAllLines() throws Exception {
        // given
        TestDataLoader testDataLoader = new TestDataLoader();
        List<LineResponse> lineResponses = LineResponse.listOf(
                Arrays.asList(testDataLoader.신분당선(), testDataLoader.이호선())
        );
        given(lineService.findLineResponses()).willReturn(lineResponses);
        //when
        mockMvc.perform(
                get("/api/lines")
        )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name")
                        .value(containsInAnyOrder("신분당선", "2호선")))
                .andExpect(jsonPath("$[*].sections[*].upStation.name")
                        .value(containsInAnyOrder("강남역", "강남역", "판교역", "역삼역")))
                .andExpect(jsonPath("$[*].sections[*].downStation.name")
                        .value(containsInAnyOrder("정자역", "잠실역", "판교역", "역삼역")))
                .andDo(print())
                .andDo(document("line-findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
    @Test
    @DisplayName("노선 ID 조회 - 성공")
    public void showLineById() throws Exception {
        // given
        TestDataLoader testDataLoader = new TestDataLoader();
        LineResponse lineResponse = LineResponse.of(testDataLoader.신분당선());
        Long id = testDataLoader.신분당선().getId();
        given(lineService.findLineResponseById(id)).willReturn(lineResponse);
        // when
        mockMvc.perform(
                get("/api/lines/" + id)
        )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("신분당선"))
                .andExpect(jsonPath("sections[*].upStation.name")
                        .value(containsInAnyOrder("강남역", "판교역")))
                .andExpect(jsonPath("sections[*].downStation.name")
                        .value(containsInAnyOrder("판교역", "정자역")))
                .andDo(print())
                .andDo(document("line-findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("노선 수정 - 성공")
    public void updateLines() throws Exception{
        final TestDataLoader testDataLoader = new TestDataLoader();
        final Line 신분당선 = testDataLoader.신분당선();
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(신분당선.getName(), 신분당선.getColor());
        given(lineService.updateLine(any(), any()))
                .willReturn(LineResponse.of(신분당선));
        mockMvc.perform(
                put("/api/lines/1")
                        .content(objectMapper.writeValueAsString(lineUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("line-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("노선 삭제 - 성공")
    public void deleteLine() throws Exception {
        // when
        mockMvc.perform(
                delete("/api/lines/1")
        )
                // then
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("line-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
    @Test
    @DisplayName("구간 추가 - 성공")
    public void createSection() throws Exception {
        // given
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 5);
        // when
        mockMvc.perform(
                post("/api/lines/1/sections")
                        .content(objectMapper.writeValueAsBytes(sectionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("section-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
    @DisplayName("구간 삭제 - 성공")
    @Test
    public void removeLineStation() throws Exception {
        // when
        mockMvc.perform(
                delete("/api/lines/1/sections?stationId=1")
        )
                // then
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("section-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}