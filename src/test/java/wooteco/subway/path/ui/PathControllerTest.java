package wooteco.subway.path.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.TestDataLoader;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.infrastructure.LoginInterceptor;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.AgeFareCalculator;
import wooteco.subway.path.application.DefaultFareCalculator;
import wooteco.subway.path.application.FareCalculator;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@WebMvcTest(controllers = PathController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
class PathControllerTest {

    @MockBean
    private PathService pathService;
    @MockBean
    private LoginInterceptor interceptor;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    private final TestDataLoader testDataLoader = new TestDataLoader();
    private final FareCalculator fareCalculator = new DefaultFareCalculator();

    @DisplayName("구간 검색 - 성공")
    @Test
    public void findPath() throws Exception {
        //given
        Line 신분당선 = testDataLoader.신분당선();
        List<Station> stations = Arrays
            .asList(testDataLoader.강남역(), testDataLoader.판교역(), testDataLoader.정자역());
        int totalDistance =
            신분당선.getSections()
                .getSections()
                .stream()
                .mapToInt(Section::getDistance)
                .sum();
        PathResponse pathResponse =
            new PathResponse(
                StationResponse.listOf(stations),
                totalDistance,
                fareCalculator.calculateFare(totalDistance, 0)
            );

        Long source = testDataLoader.강남역().getId();
        Long target = testDataLoader.정자역().getId();

        given(interceptor.preHandle(any(), any(), any())).willReturn(true);
        given(pathService.findPath(eq(source), eq(target), any(LoginMember.class)))
            .willReturn(pathResponse);

        // when
        mockMvc.perform(get("/api/paths?source=" + source + "&target=" + target))
            // then
            .andExpect(status().isOk())
            .andExpect(jsonPath("stations[*].name").value(
                Matchers.containsInRelativeOrder("강남역", "판교역", "정자역")))
            .andExpect(jsonPath("distance").value(20))
            .andExpect(jsonPath("fare").value(1450))
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
                new AgeFareCalculator(fareCalculator, 5)
                    .calculateFare(totalDistance, 0)
            );

        Long source = testDataLoader.강남역().getId();
        Long target = testDataLoader.역삼역().getId();

        given(interceptor.preHandle(any(), any(), any())).willReturn(true);
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
                new AgeFareCalculator(fareCalculator, 12)
                    .calculateFare(totalDistance, 0)
            );

        Long source = testDataLoader.강남역().getId();
        Long target = testDataLoader.역삼역().getId();

        given(interceptor.preHandle(any(), any(), any())).willReturn(true);
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
                new AgeFareCalculator(fareCalculator, 18)
                    .calculateFare(totalDistance, 0)
            );

        Long source = testDataLoader.강남역().getId();
        Long target = testDataLoader.역삼역().getId();

        given(interceptor.preHandle(any(), any(), any())).willReturn(true);
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