package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "fafi@fafi.com";
    private static final String PASSWORD = "1234";
    private static final int AGE = 27;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private StationResponse 판교역;
    private StationResponse 정자역;
    private StationResponse 역삼역;
    private StationResponse 잠실역;

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

        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        판교역 = 지하철역_등록되어_있음("판교역");
        정자역 = 지하철역_등록되어_있음("정자역");
        역삼역 = 지하철역_등록되어_있음("역삼역");
        잠실역 = 지하철역_등록되어_있음("잠실역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "black", 교대역, 강남역, 10, 1200);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "red", 교대역, 양재역, 5);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        지하철_구간_등록되어_있음(신분당선, 판교역, 강남역, 18);
        지하철_구간_등록되어_있음(신분당선, 정자역, 판교역, 47);
        지하철_구간_등록되어_있음(이호선, 강남역, 역삼역, 22);
        지하철_구간_등록되어_있음(이호선, 역삼역, 잠실역, 14);
    }

    @DisplayName("어린이, 청소년이 아닌 사용자의 경우, 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);
        ExtractableResponse<Response> response2 = 거리_경로_조회_요청(4L, 6L);
        ExtractableResponse<Response> response3 = 거리_경로_조회_요청(3L, 8L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1250);

        적절한_경로_응답됨(response2, Lists.newArrayList(남부터미널역, 양재역, 강남역, 판교역, 정자역));
        총_거리가_응답됨(response2, 77);
        총_요금이_응답됨(response2, 2450);

        적절한_경로_응답됨(response3, Lists.newArrayList(교대역, 강남역, 역삼역, 잠실역));
        총_거리가_응답됨(response3, 46);
        총_요금이_응답됨(response3, 2050 + 1200);
    }

    @DisplayName("청소년 사용자의 신분으로, 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceWhenTeenager() {
        //when
        String teenagerEmail = "teen@ager.com";
        String teenagerPassword = "teenager";
        int teenagerAge = 18;
        회원_등록되어_있음(teenagerEmail, teenagerPassword, teenagerAge);
        final TokenResponse tokenResponse = 로그인되어_있음(teenagerEmail, teenagerPassword);
        ExtractableResponse<Response> response = 토큰을_포함한_거리_경로_조회_요청(3L, 2L, tokenResponse);
        ExtractableResponse<Response> response2 = 토큰을_포함한_거리_경로_조회_요청(4L, 6L, tokenResponse);
        ExtractableResponse<Response> response3 = 토큰을_포함한_거리_경로_조회_요청(3L, 8L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1070);

        적절한_경로_응답됨(response2, Lists.newArrayList(남부터미널역, 양재역, 강남역, 판교역, 정자역));
        총_거리가_응답됨(response2, 77);
        총_요금이_응답됨(response2, 2030);

        적절한_경로_응답됨(response3, Lists.newArrayList(교대역, 강남역, 역삼역, 잠실역));
        총_거리가_응답됨(response3, 46);
        총_요금이_응답됨(response3, 2670);
    }

    @DisplayName("어린이 사용자의 신분으로, 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceWhenChildren() {
        //when
        String teenagerEmail = "child@ren.com";
        String teenagerPassword = "children";
        int teenagerAge = 12;
        회원_등록되어_있음(teenagerEmail, teenagerPassword, teenagerAge);
        final TokenResponse tokenResponse = 로그인되어_있음(teenagerEmail, teenagerPassword);
        ExtractableResponse<Response> response = 토큰을_포함한_거리_경로_조회_요청(3L, 2L, tokenResponse);
        ExtractableResponse<Response> response2 = 토큰을_포함한_거리_경로_조회_요청(4L, 6L, tokenResponse);
        ExtractableResponse<Response> response3 = 토큰을_포함한_거리_경로_조회_요청(3L, 8L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 800);

        적절한_경로_응답됨(response2, Lists.newArrayList(남부터미널역, 양재역, 강남역, 판교역, 정자역));
        총_거리가_응답됨(response2, 77);
        총_요금이_응답됨(response2, 1400);

        적절한_경로_응답됨(response3, Lists.newArrayList(교대역, 강남역, 역삼역, 잠실역));
        총_거리가_응답됨(response3, 46);
        총_요금이_응답됨(response3, 1800);
    }

    private static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 토큰을_포함한_거리_경로_조회_요청(long source, long target, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    private static void 적절한_경로_응답됨(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    private static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    private static void 총_요금이_응답됨(ExtractableResponse<Response> response, int totalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(totalFare);
    }

    private static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    private static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    private static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    private static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    private static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    private static void 지하철_구간_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        지하철_구간_생성_요청(lineResponse, upStation, downStation, distance);
    }

    private static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }
}
