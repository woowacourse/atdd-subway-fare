package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.*;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineFactory.신분당선_추가요금;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 우아한테크코스선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 에어포츈바다우기검프사랑해역;
    private StationResponse 우린모두취업할거야역;
    private StationResponse 리뷰잘부탁해요역;
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
        리뷰잘부탁해요역 = 지하철역_등록되어_있음("리뷰잘부탁해요역", tokenResponse);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-400", 강남역, 양재역, 10, 신분당선_추가요금, tokenResponse);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-500", 교대역, 강남역, 10, 0, tokenResponse);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 0, tokenResponse);
        우아한테크코스선 = 지하철_노선_등록되어_있음("우아한테크코스선", "bg-red-700", 우린모두취업할거야역, 리뷰잘부탁해요역, 15, 20_000, tokenResponse);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3, tokenResponse);
    }

    @DisplayName("두 역의 최단 거리 경로를 구하고, 최단 거리가 10km 이하인 요금을 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 양재역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1250);
    }

    private static Stream<Arguments> findFareByDistance() {
        return Stream.of(
                Arguments.of(9, 21250),
                Arguments.of(11, 21350),
                Arguments.of(50, 22050),
                Arguments.of(51, 22150),
                Arguments.of(59, 22250)
        );
    }

    @DisplayName("역 거리에 따른 요금 계산")
    @ParameterizedTest
    @MethodSource
    void findFareByDistance(int distance, int fare) {
        // given
        지하철_구간_등록되어_있음(우아한테크코스선, 에어포츈바다우기검프사랑해역, 우린모두취업할거야역, distance, tokenResponse);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(에어포츈바다우기검프사랑해역.getId(), 우린모두취업할거야역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(에어포츈바다우기검프사랑해역, 우린모두취업할거야역));
        총_거리가_응답됨(response, distance);
        총_요금이_응답됨(response, fare);
    }

    @DisplayName("여러 노선을 거칠시 가장 높은 노선 추가금액을 적용하여 계산")
    @Test
    void findHighestLineFareByDistance() {
        // given
        LineResponse 배달의민족선 = 지하철_노선_등록되어_있음("배달의민족선", "bg-red-800", 양재역, 리뷰잘부탁해요역, 3, 2000, tokenResponse);
        지하철_구간_등록되어_있음(우아한테크코스선, 에어포츈바다우기검프사랑해역, 우린모두취업할거야역, 13, tokenResponse);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 리뷰잘부탁해요역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 리뷰잘부탁해요역));
        총_거리가_응답됨(response, 13);
        총_요금이_응답됨(response, 3350);
    }

    @DisplayName("청소년 (13세 이상 ~ 19세 미만)인 경우 20% 할인된 금액이 적용된다. ")
    @Test
    void findFareByDistanceWithLogin1() {
        // given
        double discountRate = 0.8;
        회원_등록되어_있음("청소년포츈@naver.com","오랑해요포츈", 13);
        TokenResponse tokenResponse = 로그인되어_있음("청소년포츈@naver.com", "오랑해요포츈");
        LineResponse 배달의민족선 = 지하철_노선_등록되어_있음("배달의민족선", "bg-red-800", 양재역, 리뷰잘부탁해요역, 3, 2000, tokenResponse);
        지하철_구간_등록되어_있음(우아한테크코스선, 에어포츈바다우기검프사랑해역, 우린모두취업할거야역, 13, tokenResponse);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 리뷰잘부탁해요역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 리뷰잘부탁해요역));
        총_거리가_응답됨(response, 13);
        총_요금이_응답됨(response, (int) ((3350 - 350) * discountRate));
    }

    @DisplayName("청소년 (6세 이상 ~ 13세 미만)인 경우 50% 할인된 금액이 적용된다. ")
    @Test
    void findFareByDistanceWithLogin2() {
        // given
        double discountRate = 0.5;
        회원_등록되어_있음("아가검프@naver.com","오랑해요포츈", 6);
        TokenResponse tokenResponse = 로그인되어_있음("아가검프@naver.com", "오랑해요포츈");
        LineResponse 배달의민족선 = 지하철_노선_등록되어_있음("배달의민족선", "bg-red-800", 양재역, 리뷰잘부탁해요역, 3, 2000, tokenResponse);
        지하철_구간_등록되어_있음(우아한테크코스선, 에어포츈바다우기검프사랑해역, 우린모두취업할거야역, 13, tokenResponse);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 리뷰잘부탁해요역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 리뷰잘부탁해요역));
        총_거리가_응답됨(response, 13);
        총_요금이_응답됨(response, (int) ((3350 - 350) * discountRate));
    }

    @Test
    void findExtraFare() {
        // given
        int expectedTotalFare = 21_350;

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(우린모두취업할거야역.getId(), 리뷰잘부탁해요역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(우린모두취업할거야역, 리뷰잘부탁해요역));
        총_거리가_응답됨(response, 15);
        총_요금이_응답됨(response, expectedTotalFare);
    }

    @DisplayName("departure와 arrival가 같은경우 예외가 발생한다.")
    @Test
    void samePositionException() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 교대역.getId(), tokenResponse);

        //then
        거리_경로_조회_요청_실패(response);
    }

    @DisplayName("departure와 arrival가 이어지지 않는 경우 예외발생한다.")
    @Test
    void noPathException() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 우린모두취업할거야역.getId(), tokenResponse);

        //then
        거리_경로_조회_요청_실패(response);
    }

    @DisplayName("departure와 arrival중 하나라도 노선에 등록되어 있지 않을 시 예외발생한다.")
    @Test
    void noInLineStationException() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 에어포츈바다우기검프사랑해역.getId(), tokenResponse);

        //then
        거리_경로_조회_요청_실패(response);
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

    private void 총_요금이_응답됨(ExtractableResponse<Response> response, int expectedFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }

    private void 거리_경로_조회_요청_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
