package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.*;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 에어포츈바다우기검프사랑해역;
    private StationResponse 우린모두취업할거야역;
    private TokenResponse tokenResponse;

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
        tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        양재역 = 지하철역_등록되어_있음("양재역", tokenResponse);
        교대역 = 지하철역_등록되어_있음("교대역", tokenResponse);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역", tokenResponse);
        에어포츈바다우기검프사랑해역 = 지하철역_등록되어_있음("에어포츈바다우기검프사랑해역", tokenResponse);
        우린모두취업할거야역 = 지하철역_등록되어_있음("우린모두취업할거야역", tokenResponse);
//        StationResponse 리뷰잘부탁해요역 = 지하철역_등록되어_있음("리뷰잘부탁해요역", tokenResponse);

        //LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-400", 강남역, 양재역, 10, 100, tokenResponse);
        //LineResponse 이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-500", 교대역, 강남역, 10, 200, tokenResponse);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 300, tokenResponse);
        //LineResponse 우아한테크코스선 = 지하철_노선_등록되어_있음("우아한테크코스선", "bg-red-700", 우린모두취업할거야역, 리뷰잘부탁해요역, 5, 400, tokenResponse);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3, tokenResponse);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 비로그인")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청_비로그인(교대역.getId(), 양재역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1550);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 로그인")
    @ParameterizedTest
    @CsvSource(value = {"'1@email.com', '1234', 13, 960", "'2@email.com', '1234', 12, 600", "'3@email.com', '1234', 19, 1550"})
    void findPathByDistanceLogin(String email, String password, int age, int fare) {
        //when
        회원_등록되어_있음(email, password, age);
        TokenResponse tokenResponse = 로그인되어_있음(email, password);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, fare);
    }

    @DisplayName("departure와 arrival이 같은 경우, 400 에러를 받는다.")
    @Test
    void samePositionException() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 교대역.getId(), tokenResponse);

        //then
        거리_경로_조회_요청_실패(response);
    }

    @DisplayName("departure와 arrival이 이어지지 않은 경우, 400 에러를 받는다.")
    @Test
    void noPathException() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 우린모두취업할거야역.getId(), tokenResponse);

        //then
        거리_경로_조회_요청_실패(response);
    }

    @DisplayName("departure와 arrival 중 하나라도 노선에 등록되어 있지 않을 시, 400 에러를 받는다.")
    @Test
    void noInLineStationException() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 에어포츈바다우기검프사랑해역.getId(), tokenResponse);

        //then
        거리_경로_조회_요청_실패(response);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청_비로그인(long departure, long arrival) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?departure={departureId}&arrival={arrivalId}", departure, arrival)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long departure, long arrival, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?departure={departureId}&arrival={arrivalId}", departure, arrival)
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

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    public static void 총_요금이_응답됨(ExtractableResponse<Response> response, int totalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(totalFare);
    }

    private void 거리_경로_조회_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}