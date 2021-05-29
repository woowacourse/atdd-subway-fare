package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.auth.acceptance.AuthAcceptanceTest.토큰;
import static wooteco.subway.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.acceptance.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.AcceptanceTest;
import wooteco.auth.web.dto.response.TokenResponse;
import wooteco.subway.web.dto.request.LineRequest;
import wooteco.subway.web.dto.response.LineResponse;
import wooteco.subway.web.dto.response.PathResponse;
import wooteco.subway.web.dto.response.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 추가요금선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 새로운역;
    private StationResponse 남부터미널역;

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target,
        TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    public static void 적절한_경로_응답됨(ExtractableResponse<Response> response,
        ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    public static void 요금_비교(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    /**
     * 교대역    --- *2호선* ---   강남역 |                        | *3호선*                   *신분당선* |
     * | 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        새로운역 = 지하철역_등록되어_있음("새로운역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-pink-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-yellow-600", 교대역, 양재역, 5);
        추가요금선 = 지하철_노선_등록되어_있음(
            new LineRequest("추가요금선", "bg-add-500", 남부터미널역.getId(), 새로운역.getId(), 4, 300));

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
    }

    @DisplayName("두 역의 경로의 요금을 확인한다.")
    @Test
    void findPathWithFare() {
        // 비회원 시
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);
        요금_비교(response, 1250);

        // 2살 때
        final TokenResponse token1 = 토큰("test2@test.com", "password", 2);
        final ExtractableResponse<Response> response2 = 거리_경로_조회_요청(3L, 2L, token1);
        요금_비교(response2, 0);

        // 8살 때
        final TokenResponse token2 = 토큰("test8@test.com", "password", 8);
        final ExtractableResponse<Response> response3 = 거리_경로_조회_요청(3L, 2L, token2);
        요금_비교(response3, 450);

        // 16살 때
        final TokenResponse token3 = 토큰("test16@test.com", "password", 16);
        final ExtractableResponse<Response> response4 = 거리_경로_조회_요청(3L, 2L, token3);
        요금_비교(response4, 720);

        // 성인 요금
        final TokenResponse token4 = 토큰("test29@test.com", "password", 29);
        final ExtractableResponse<Response> response5 = 거리_경로_조회_요청(3L, 2L, token4);
        요금_비교(response5, 1250);

        // 추가 요금선 + 성인 요금
        final ExtractableResponse<Response> response6 = 거리_경로_조회_요청(남부터미널역.getId(), 새로운역.getId(),
            token4);
        요금_비교(response6, 1550);
    }
}
