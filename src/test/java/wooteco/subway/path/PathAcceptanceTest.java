package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
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
import static wooteco.subway.line.LineAcceptanceTest.로그인_사용자_지하철_추가요금_노선_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.로그인_사용자_지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
@Sql("classpath:tableInit.sql")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private TokenResponse tokenResponse;

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청_인증_헤더(long source, long target) {
        AuthAcceptanceTest.회원_등록되어_있음("email@email.com", "1234", 15);
        final TokenResponse tokenResponse
                = AuthAcceptanceTest.로그인되어_있음("email@email.com", "1234");

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

    public static void 총_금액이_응답됨(ExtractableResponse<Response> response, int totalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(totalFare);
    }

    /**
     * 10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선* 11
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        AuthAcceptanceTest.회원_등록되어_있음("email@email.com", "1234", 15);
        tokenResponse = AuthAcceptanceTest.로그인되어_있음("email@email.com", "1234");

        강남역 = 로그인_사용자_지하철역_등록되어_있음(tokenResponse, "강남역");
        양재역 = 로그인_사용자_지하철역_등록되어_있음(tokenResponse, "양재역");
        교대역 = 로그인_사용자_지하철역_등록되어_있음(tokenResponse, "교대역");
        남부터미널역 = 로그인_사용자_지하철역_등록되어_있음(tokenResponse, "남부터미널역");

        신분당선 = 로그인_사용자_지하철_추가요금_노선_등록되어_있음(tokenResponse, "신분당선", "bg-red-600", 강남역, 양재역, 11, 900);
        이호선 = 로그인_사용자_지하철_추가요금_노선_등록되어_있음(tokenResponse, "이호선", "bg-black-600", 교대역, 강남역, 10, 1100);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 강남역, 양재역));
        총_거리가_응답됨(response, 21);
        총_금액이_응답됨(response, 1250 + 1100 + 300);
    }

    @DisplayName("초과 운임을 고려하여 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceWithOverFare() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역));
        총_거리가_응답됨(response, 11);
        총_금액이_응답됨(response, 2250);
    }

    @DisplayName("비로그인 사용자가 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceWhenNotLogin() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 강남역, 양재역));
        총_거리가_응답됨(response, 21);
        총_금액이_응답됨(response, 2650);
    }

    @DisplayName("로그인 사용자가 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceWhenLogin() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청_인증_헤더(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 강남역, 양재역));
        총_거리가_응답됨(response, 21);
        총_금액이_응답됨(response, 1840);
    }
}
