package wooteco.subway.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.util.JwtTokenProvider;
import wooteco.auth.web.api.LoginInterceptor;
import wooteco.subway.TestDataLoader;
import wooteco.subway.domain.Station;
import wooteco.subway.service.AgeDiscountFareCalculator;
import wooteco.subway.service.DefaultFareCalculator;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.service.PathService;
import wooteco.subway.web.api.PathController;
import wooteco.subway.web.dto.response.PathResponse;
import wooteco.subway.web.dto.response.StationResponse;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PathController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class PathControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PathService pathService;
    @MockBean
    private LoginInterceptor loginInterceptor;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private TestDataLoader testDataLoader = new TestDataLoader();
    private FareCalculator fareCalculator = new DefaultFareCalculator();

    @Test
    @DisplayName("구간 검색 - 성공(성인 또는 비회원)")
    public void findPath() throws Exception {
        //given
        int totalDistance = 10;
        List<Station> stations = Arrays
                .asList(testDataLoader.강남역(), testDataLoader.역삼역());
        PathResponse pathResponse =
                new PathResponse(
                        StationResponse.listOf(stations),
                        totalDistance,
                        new AgeDiscountFareCalculator(fareCalculator, 20)
                                .calculateFare(totalDistance, 0)
                );
        Long source = testDataLoader.강남역().getId();
        Long target = testDataLoader.역삼역().getId();
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(pathService.findPath(eq(source), eq(target), any(LoginMember.class)))
                .willReturn(pathResponse);

        //when
        mockMvc.perform(get("/api/paths?source=" + source + "&target=" + target))
                .andExpect(status().isOk())
                .andExpect(jsonPath("stations[*].name").value(
                        Matchers.containsInRelativeOrder("강남역", "역삼역")))
                .andExpect(jsonPath("fare").value(1250))
                .andDo(document("path-find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("구간 검색 - 성공(6세 미만 요금 할인)")
    @Test
    public void findPath_2() throws Exception {
        //given
        int totalDistance = 10;
        List<Station> stations = Arrays
                .asList(testDataLoader.강남역(), testDataLoader.역삼역());
        PathResponse pathResponse =
                new PathResponse(
                        StationResponse.listOf(stations),
                        totalDistance,
                        new AgeDiscountFareCalculator(fareCalculator, 5)
                                .calculateFare(totalDistance, 0)
                );
        Long source = testDataLoader.강남역().getId();
        Long target = testDataLoader.역삼역().getId();
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(pathService.findPath(eq(source), eq(target), any(LoginMember.class)))
                .willReturn(pathResponse);
        // when
        mockMvc.perform(get("/api/paths?source=" + source + "&target=" + target))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("stations[*].name").value(
                        Matchers.containsInRelativeOrder("강남역", "역삼역")))
                .andExpect(jsonPath("distance").value(10))
                .andExpect(jsonPath("fare").value(0));
    }
    @DisplayName("구간 검색 - 성공(6-13세 요금 할인)")
    @Test
    public void findPath_3() throws Exception {
        //given
        int totalDistance = 10;
        List<Station> stations = Arrays
                .asList(testDataLoader.강남역(), testDataLoader.역삼역());
        PathResponse pathResponse =
                new PathResponse(
                        StationResponse.listOf(stations),
                        totalDistance,
                        new AgeDiscountFareCalculator(fareCalculator, 12)
                                .calculateFare(totalDistance, 0)
                );
        Long source = testDataLoader.강남역().getId();
        Long target = testDataLoader.역삼역().getId();
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(pathService.findPath(eq(source), eq(target), any(LoginMember.class)))
                .willReturn(pathResponse);
        // when
        mockMvc.perform(get("/api/paths?source=" + source + "&target=" + target))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("stations[*].name").value(
                        Matchers.containsInRelativeOrder("강남역", "역삼역")))
                .andExpect(jsonPath("distance").value(10))
                .andExpect(jsonPath("fare").value(450));
    }
    @DisplayName("구간 검색 - 성공(14-19세 요금 할인)")
    @Test
    public void findPath_4() throws Exception {
        //given
        int totalDistance = 10;
        List<Station> stations = Arrays
                .asList(testDataLoader.강남역(), testDataLoader.역삼역());
        PathResponse pathResponse =
                new PathResponse(
                        StationResponse.listOf(stations),
                        totalDistance,
                        new AgeDiscountFareCalculator(fareCalculator, 18)
                                .calculateFare(totalDistance, 0)
                );
        Long source = testDataLoader.강남역().getId();
        Long target = testDataLoader.역삼역().getId();
        given(loginInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(pathService.findPath(eq(source), eq(target), any(LoginMember.class)))
                .willReturn(pathResponse);
        // when
        mockMvc.perform(get("/api/paths?source=" + source + "&target=" + target))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("stations[*].name").value(
                        Matchers.containsInRelativeOrder("강남역", "역삼역")))
                .andExpect(jsonPath("distance").value(10))
                .andExpect(jsonPath("fare").value(720));
    }
}