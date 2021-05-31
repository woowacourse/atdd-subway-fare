package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.member.MemberAcceptanceTest.*;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        교대역 = createStation("교대역");
        남부터미널역 = createStation("남부터미널역");

        신분당선 = createLine(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));
        이호선 = createLine(new LineRequest("이호선", "bg-red-600", 500, 교대역.getId(), 강남역.getId(), 10));
        삼호선 = createLine(new LineRequest("삼호선", "bg-red-600", 800, 교대역.getId(), 양재역.getId(), 5));

        addSection(삼호선.getId(), new SectionRequest(교대역.getId(), 남부터미널역.getId(), 3));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = findPath(3L, 2L, new TokenResponse(""));

        //then
        assertPath(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertDistance(response, 5);
    }

    @DisplayName("두 역의 경로 거리와 로그인 여부를 바탕으로 요금을 계산한다")
    @Test
    void fare() {
        createMember(EMAIL, PASSWORD, 13);
        TokenResponse tokenResponse1 = login(EMAIL, PASSWORD);
        createMember(NEW_EMAIL, PASSWORD, 12);
        TokenResponse tokenResponse2 = login(NEW_EMAIL, PASSWORD);

        int expectedTotalFare = 1250 + 삼호선.getExtraFare() + 100;

        ExtractableResponse<Response> responseWithAge13 = findPath(4L, 1L, tokenResponse1);
        ExtractableResponse<Response> responseWithAge12 = findPath(4L, 1L, tokenResponse2);
        ExtractableResponse<Response> responseWithoutLogin = findPath(4L, 1L, new TokenResponse(""));

        PathResponse pathResponseWithAge13 = responseWithAge13.as(PathResponse.class);
        assertThat(pathResponseWithAge13.getFare()).isEqualTo(expectedTotalFare - (int) ((expectedTotalFare - 350) * 0.2));

        PathResponse pathResponseWithAge12 = responseWithAge12.as(PathResponse.class);
        assertThat(pathResponseWithAge12.getFare()).isEqualTo(expectedTotalFare - (int) ((expectedTotalFare - 350) * 0.5));

        PathResponse pathResponseWithoutLogin = responseWithoutLogin.as(PathResponse.class);
        assertThat(pathResponseWithoutLogin.getFare()).isEqualTo(expectedTotalFare);
    }

    private void assertPath(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }
}
