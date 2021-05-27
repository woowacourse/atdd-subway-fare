package wooteco.subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import wooteco.auth.web.api.LoginInterceptor;
import wooteco.common.ExceptionAdvice;
import wooteco.auth.service.AuthService;
import wooteco.common.exception.badrequest.StationDuplicateNameException;
import wooteco.common.exception.unauthorizationexception.UnAuthorizationException;
import wooteco.subway.service.StationService;
import wooteco.subway.web.dto.request.StationRequest;
import wooteco.subway.web.dto.response.StationResponse;
import wooteco.subway.web.api.StationController;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {StationController.class, ExceptionAdvice.class})
@ActiveProfiles("test")
@AutoConfigureRestDocs
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StationService stationService;

    @MockBean
    private LoginInterceptor loginInterceptor;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("역 생성 - 성공")
    public void create() throws Exception {
        //given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        given(stationService.saveStation(any(StationRequest.class))).willReturn(stationResponse);
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        mockMvc.perform(post("/api/stations")
                .content(objectMapper.writeValueAsBytes(stationRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("id").value(stationResponse.getId()))
                .andExpect(jsonPath("name").value(stationResponse.getName()))
                .andDo(print())
                .andDo(document("station-create"));
    }

    @DisplayName("역 생성 - 실패(인증)")
    @Test
    public void createStationFail() throws Exception {
        //given
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse stationResponse = new StationResponse(1L, "잠실역");

        given(stationService.saveStation(any(StationRequest.class))).willReturn(stationResponse);
        given(loginInterceptor.preHandle(any(), any(), any()))
                .willThrow(UnAuthorizationException.class);

        //when
        mockMvc.perform(post("/api/stations")
                .content(objectMapper.writeValueAsString(stationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                // then
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("station-create-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("역 조회 - 성공")
    public void show() throws Exception {
        //given
        List<StationResponse> stationResponses = Arrays.asList(
                new StationResponse(1L, "잠실역"),
                new StationResponse(2L, "강남역"),
                new StationResponse(3L, "사당역"),
                new StationResponse(4L, "잠실새내역"),
                new StationResponse(5L, "종합운동장역")
        );
        given(stationService.findAllStationResponses())
                .willReturn(stationResponses);
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        mockMvc.perform(get("/api/stations")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.*.name")
                        .value(Matchers.containsInAnyOrder("잠실역", "강남역", "사당역", "잠실새내역", "종합운동장역")))
                .andDo(print())
                .andDo(document("station-find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("역 수정 - 성공")
    @Test
    public void updateStationSuccess() throws Exception {
        // given
        String name = "잠실역";
        StationResponse stationResponse = new StationResponse(1L, "잠실역");
        given(stationService.updateStation(any(Long.class), any(String.class)))
                .willReturn(stationResponse);
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        //when
        mockMvc.perform(put("/api/stations/1")
                .content(objectMapper.writeValueAsString(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(stationResponse.getId()))
                .andExpect(jsonPath("name").value(stationResponse.getName()))
                .andDo(print())
                .andDo(document("station-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("역 수정 - 실패(중복 이름)")
    public void updateStations() throws Exception {
        given(stationService.updateStation(any(), any()))
                .willThrow(new StationDuplicateNameException("newName"));
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        final StationRequest stationRequest = new StationRequest("newName");
        mockMvc.perform(put("/api/stations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stationRequest))
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("newName은 이미 존재하는 역 이름입니다."))
                .andDo(document("station-update-duplicate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("역 삭제 - 성공")
    public void deleteStation() throws Exception {
        //given
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);

        //when
        mockMvc.perform(delete("/api/stations/1")
        )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("station-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
