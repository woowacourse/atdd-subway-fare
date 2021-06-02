package wooteco.subway.acceptance;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.AcceptanceTest;
import wooteco.auth.web.dto.response.TokenResponse;
import wooteco.subway.web.dto.response.LineResponse;
import wooteco.subway.web.dto.response.PathResponse;
import wooteco.subway.web.dto.response.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.auth.acceptatnce.AuthAcceptanceTest.토큰;
import static wooteco.subway.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.acceptance.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 추가요금선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 추가요금역;
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
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");
        추가요금역 = 지하철역_등록되어_있음("추가요금역");
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-yellow-600", 교대역, 양재역, 5);
        추가요금선 = 지하철_노선_등록되어_있음("추가요금선", "bg-black-600", 남부터미널역, 추가요금역, 30, 900);
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

    @Test
    @DisplayName("두 역 거리 경로의 요금을 조회한다.")
    public void findPathWithFare() {
        //비회원일 때
        ExtractableResponse<Response> response1 = 거리_경로_조회_요청(3L, 2L);
        요금_조회(response1, 1250);

        //6세 미만
        TokenResponse tokenResponse1 = 토큰("test@test.com", "12341234", 3);
        ExtractableResponse<Response> response2 = 거리_경로_조회_요청(3L, 2L, tokenResponse1);
        요금_조회(response2, 0);

        //6세 이상 13세 미만
        TokenResponse tokenResponse2 = 토큰("test1@test.com", "12341234", 6);
        ExtractableResponse<Response> response3 = 거리_경로_조회_요청(3L, 2L, tokenResponse2);
        요금_조회(response3, 450);

        //13세 이상
        TokenResponse tokenResponse3 = 토큰("test2@test.com", "12341234", 13);
        ExtractableResponse<Response> response4 = 거리_경로_조회_요청(3L, 2L, tokenResponse3);
        요금_조회(response4, 720);

        //19세 이상
        TokenResponse tokenResponse4 = 토큰("test3@test.com", "12341234", 19);
        ExtractableResponse<Response> response5 = 거리_경로_조회_요청(3L, 2L, tokenResponse4);
        요금_조회(response5, 1250);

        //6세 이상 13세 미만인데 추가요금이 있을 때
        ExtractableResponse<Response> response6 = 거리_경로_조회_요청(4L, 5L, tokenResponse2);
        요금_조회(response6, 1100);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 적절한_경로_응답됨(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(pathResponse.getFare()).isEqualTo(1250);
        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    private static void 요금_조회(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }
}