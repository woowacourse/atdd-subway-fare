package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("노선 관련 테스트")
@WebMvcTest(controllers = LineController.class)
public class LineControllerTest extends ControllerTest {
    @MockBean
    LineService lineService;
    @MockBean
    AuthService authService;

    @Test
    @DisplayName("생성 - 성공")
    void create() throws Exception {
        long lineId = 1L;
        LineRequest lineRequest = new LineRequest("테스트선", "주황색", 1L, 2L, 10, 100);
        LineResponse lineResponse = new LineResponse(lineId, "테스트선", "주황색", Collections.emptyList(), Collections.emptyList());
        String json = new ObjectMapper().writeValueAsString(lineRequest);
        given(lineService.saveLine(any(LineRequest.class))).willReturn(lineResponse);
        mockMvc.perform(post("/lines")
                .content(json)
                .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/lines/" + lineId))
                .andExpect(content().json(objectMapper.writeValueAsString(lineResponse)))
                .andDo(print())
                .andDo(document("line-create"));
    }

    @Test
    @DisplayName("전체 조회 - 성공")
    void findAllLines() throws Exception {
        List<LineResponse> lineResponses = Arrays.asList(
                new LineResponse(1L, "테스트1선", "주황색", Collections.emptyList(), Collections.emptyList()),
                new LineResponse(2L, "테스트2선", "주황색", Collections.emptyList(), Collections.emptyList())
        );
        given(lineService.findLineResponses()).willReturn(lineResponses);
        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andDo(print())
                .andDo(document("line-find"));
    }

    @Test
    @DisplayName("id 조회 - 성공")
    void findById() throws Exception {
        LineResponse lineResponse = new LineResponse(1L, "테스트1선", "주황색", Collections.emptyList(), Collections.emptyList());
        long lineId = 1L;
        given(lineService.findLineResponseById(lineId)).willReturn(lineResponse);
        mockMvc.perform(get("/lines/" + lineId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lineResponse)))
                .andDo(print())
                .andDo(document("line-findbyid"));
    }

    @Test
    @DisplayName("수정 - 성공")
    void update() throws Exception {
        long lineId = 1L;
        LineRequest lineRequest = new LineRequest("테스트선", "주황색", 1L, 2L, 10, 100);
        String json = new ObjectMapper().writeValueAsString(lineRequest);
        mockMvc.perform(put("/lines/" + lineId)
                .content(json)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("line-update"));
    }

    @Test
    @DisplayName("삭제 - 성공")
    void deleteLine() throws Exception {
        long lineId = 1L;
        mockMvc.perform(delete("/lines/" + lineId))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("line-delete"));
    }

    @Test
    @DisplayName("구간 추가 - 성공")
    void addSection() throws Exception {
        long lineId = 1L;
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        String json = new ObjectMapper().writeValueAsString(sectionRequest);
        mockMvc.perform(post("/lines/" + lineId + "/sections")
                .content(json)
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("section-create"));
    }

    @Test
    @DisplayName("구간 삭제 - 성공")
    void deleteSection() throws Exception {
        long lineId = 1L;
        long stationId = 1L;
        mockMvc.perform(delete("/lines/" + lineId + "/sections?stationId=" + stationId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("section-delete"));
    }
}
