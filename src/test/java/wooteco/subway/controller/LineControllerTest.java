package wooteco.subway.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.auth.util.JwtTokenProvider;
import wooteco.subway.TestDataLoader;
import wooteco.subway.domain.Line;
import wooteco.subway.service.LineService;
import wooteco.subway.web.api.LineController;
import wooteco.subway.web.dto.LineResponseAssembler;
import wooteco.subway.web.dto.request.LineRequest;
import wooteco.subway.web.dto.request.LineUpdateRequest;
import wooteco.subway.web.dto.request.SectionRequest;
import wooteco.subway.web.dto.response.LineResponse;
import wooteco.subway.web.dto.response.StationWithDistanceResponse;

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
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("노선 생성 - 성공")
    public void createLines() throws Exception {
        //given
        final LineRequest lineRequest = new LineRequest("2호선", "bg-red-200", 1L, 2L, 5);

        final List<StationWithDistanceResponse> stationWithDistanceResponses = Arrays
            .asList(new StationWithDistanceResponse(1L, "강남역", 3),
                new StationWithDistanceResponse(2L, "역삼역")
            );

        final LineResponse lineResponse = new LineResponse(1L, "2호선", "bg-red-200",
            stationWithDistanceResponses);

        given(lineService.saveLine(any(LineRequest.class)))
            .willReturn(lineResponse);

        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);

        mockMvc.perform(post("/api/lines")
            .header("Authorization", "Bearer " + token)
            .content(objectMapper.writeValueAsString(lineRequest))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("name").value(lineResponse.getName()))
            .andExpect(jsonPath("color").value(lineResponse.getColor()))
            .andDo(print())
            .andDo(document("line-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("노선 조회 - 성공")
    public void findLines() throws Exception {
        final TestDataLoader testDataLoader = new TestDataLoader();
        final List<LineResponse> lineResponses = Arrays
            .asList(LineResponseAssembler.assemble(testDataLoader.신분당선()),
                LineResponseAssembler.assemble(testDataLoader.이호선()));
        given(lineService.findLineResponses()).willReturn(lineResponses);
        //given
        mockMvc.perform(get("/api/lines"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("신분당선", "2호선")))
            .andDo(document("line-find",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("노선 수정 - 성공")
    public void updateLines() throws Exception {
        final TestDataLoader testDataLoader = new TestDataLoader();
        final Line 신분당선 = testDataLoader.신분당선();
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(신분당선.getName(),
            신분당선.getColor());
        given(lineService.findLineResponseById(any()))
            .willReturn(LineResponseAssembler.assemble(신분당선));

        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);

        mockMvc.perform(
            put("/api/lines/1")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(lineUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("lines-update",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("노선 삭제 - 성공")
    public void deleteLine() throws Exception {
        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);

        mockMvc.perform(
            delete("/api/lines/1")
                .header("Authorization", "Bearer " + token)
        )
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("line-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @DisplayName("구간 삭제 - 성공")
    @Test
    public void removeLineStation() throws Exception {
        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);

        mockMvc.perform(delete("/api/lines/1/sections?stationId=1")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(document("section-delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("노선 ID 조회 - 성공")
    public void showLineById() throws Exception {
        final TestDataLoader testDataLoader = new TestDataLoader();
        final LineResponse lineResponse = LineResponseAssembler.assemble(testDataLoader.신분당선());
        Long id = testDataLoader.신분당선().getId();
        given(lineService.findLineResponseById(id)).willReturn(lineResponse);
        mockMvc.perform(get("/api/lines/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("name").value("신분당선"))
            .andDo(print())
            .andDo(document("line-findbyid",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("구간 추가 - 성공")
    public void createSection() throws Exception {
        String token = "이것은토큰입니다";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);

        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 5);
        mockMvc.perform(post("/api/lines/1/sections")
            .header("Authorization", "Bearer " + token)
            .content(objectMapper.writeValueAsBytes(sectionRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("section-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

}