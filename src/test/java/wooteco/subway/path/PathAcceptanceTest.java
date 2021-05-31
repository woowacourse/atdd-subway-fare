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
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.AuthAcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private LineResponse 오호선;
    private LineResponse 육호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 잠실역;
    private StationResponse 옥수역;
    private StationResponse 코다역;
    private StationResponse 피카역;
    private StationResponse 마크역;

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

        tokenResponse = AuthAcceptanceTest.회원가입_토큰가져오기();

        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        양재역 = 지하철역_등록되어_있음("양재역", tokenResponse);
        교대역 = 지하철역_등록되어_있음("교대역", tokenResponse);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역", tokenResponse);
        잠실역 = 지하철역_등록되어_있음("잠실역", tokenResponse);
        옥수역 = 지하철역_등록되어_있음("옥수역", tokenResponse);
        코다역 = 지하철역_등록되어_있음("수원역", tokenResponse);
        피카역 = 지하철역_등록되어_있음("피카역", tokenResponse);
        마크역 = 지하철역_등록되어_있음("마크역", tokenResponse);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, tokenResponse);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10, tokenResponse);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-blue-600", 교대역, 양재역, 5, tokenResponse);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-purple-600", 남부터미널역, 잠실역, 40, tokenResponse);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-white-600", 피카역, 마크역, 3, 500, tokenResponse);
        육호선 = 지하철_노선_등록되어_있음("육호선", "bg-black-600", 마크역, 코다역, 5, 900, tokenResponse);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3, tokenResponse);
        지하철_구간_등록되어_있음(삼호선, 남부터미널역, 잠실역, 30, tokenResponse);
        지하철_구간_등록되어_있음(사호선, 잠실역, 옥수역, 30, tokenResponse);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("거리가 10이상 50이하 일 경우 요금 조회")
    @Test
    void findFareOverTenDistance() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(남부터미널역.getId(), 잠실역.getId(), tokenResponse);

        총_거리가_응답됨(response, 40);
        총_요금이_응답됨(response, 1850);
    }

    @DisplayName("거리가 50이상 일 경우 요금 조회")
    @Test
    void findFareOverFiftyDistance() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(남부터미널역.getId(), 옥수역.getId(), tokenResponse);

        총_거리가_응답됨(response, 70);
        총_요금이_응답됨(response, 2350);
    }

    @DisplayName("노선에 요금이 추가될 경우 요금 조회")
    @Test
    void findFareWithLineFare() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(피카역.getId(), 마크역.getId(), tokenResponse);

        총_요금이_응답됨(response, 1750);
    }

    @DisplayName("노선 요금이 여러 개일 경우 제일 높은 요금 추가 요금 조회")
    @Test
    void findFareWithLinesFare() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(피카역.getId(), 코다역.getId(), tokenResponse);

        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("거리가 10 이하일 경우 나이에 따라 요금이 달라진다")
    @ParameterizedTest
    @CsvSource(value = {"6:800", "13:1070", "19:1250"}, delimiterString = ":")
    void findFareWithDefaultDistanceEachAge(int age, int fare) {
        TokenResponse tokenResponse = 나이에_따라_로그인(age);

        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 양재역.getId(), tokenResponse);

        총_요금이_응답됨(response, fare);
    }

    @DisplayName("거리가 50 이하일 경우 나이에 따라 요금이 달라진다")
    @ParameterizedTest
    @CsvSource(value = {"6:1100", "13:1550", "19:1850"}, delimiterString = ":")
    void findFareWithFiftyUnderDistanceEachAge(int age, int fare) {
        TokenResponse tokenResponse = 나이에_따라_로그인(age);

        ExtractableResponse<Response> response = 거리_경로_조회_요청(남부터미널역.getId(), 잠실역.getId(), tokenResponse);

        총_요금이_응답됨(response, fare);
    }

    @DisplayName("거리가 50 이상일 경우 나이에 따라 요금이 달라진다")
    @ParameterizedTest
    @CsvSource(value = {"6:1350", "13:1950", "19:2350"}, delimiterString = ":")
    void findFareWithFiftyOverDistanceEachAge(int age, int fare) {
        TokenResponse tokenResponse = 나이에_따라_로그인(age);

        ExtractableResponse<Response> response = 거리_경로_조회_요청(남부터미널역.getId(), 옥수역.getId(), tokenResponse);

        총_요금이_응답됨(response, fare);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
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

    private static TokenResponse 나이에_따라_로그인(int age) {
        회원_등록되어_있음("email@naver.com", "password", age);
        TokenResponse tokenResponse = AuthAcceptanceTest.로그인되어_있음("email@naver.com", "password");
        return tokenResponse;
    }
}
