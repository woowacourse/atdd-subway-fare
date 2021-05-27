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
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 잠실역;
    private StationResponse 석촌역;

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
        잠실역 = 지하철역_등록되어_있음("잠실역");
        석촌역 = 지하철역_등록되어_있음("석촌역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900L);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 0L);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 0L);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        지하철_구간_등록되어_있음(신분당선, 양재역, 잠실역, 40);
        지하철_구간_등록되어_있음(신분당선, 잠실역, 석촌역, 40);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 양재역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
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

    public static void 총_요금이_응답됨(ExtractableResponse<Response> response, double fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    @Test
    @DisplayName("주어진 경로의 요금을 계산한다. - 기본 운임")
    public void calculateFareByDistanceBasic() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 남부터미널역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역));
        총_요금이_응답됨(response, 1250);
    }

    @Test
    @DisplayName("주어진 경로의 요금을 계산한다.10KM - 50KM")
    public void calculateFareByDistance_10km_50km() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 잠실역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 잠실역));
        총_요금이_응답됨(response, 2950);
    }

    @Test
    @DisplayName("주어진 경로의 요금을 계산한다. 50KM 초과")
    public void calculateFareByDistance_50km_over() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 석촌역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 잠실역, 석촌역));
        총_요금이_응답됨(response, 3450);
    }

    @Test
    @DisplayName("로그인이 되어 있는 경우 경로 조회 - 성인")
    public void calculateFareByAgeAdult() {
        //given
        회원_등록되어_있음("adult@adult", "adult", 19);
        TokenResponse tokenResponse = 로그인되어_있음("adult@adult", "adult");

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 석촌역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 잠실역, 석촌역));
        총_요금이_응답됨(response,  3450);
    }

    @Test
    @DisplayName("로그인이 되어 있는 경우 경로 조회 - 청소년")
    public void calculateFareByAgeTeenager() {
        //given
        회원_등록되어_있음("teenager@teenager", "teenager", 13);
        TokenResponse tokenResponse = 로그인되어_있음("teenager@teenager", "teenager");

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 석촌역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 잠실역, 석촌역));
        총_요금이_응답됨(response,  3450 - ((3450 - 350) * 0.2));
    }

    @Test
    @DisplayName("로그인이 되어 있는 경우 경로 조회 - 어린이")
    public void calculateFareByAgeKid() {
        //given
        회원_등록되어_있음("kid@kid", "kid", 12);
        TokenResponse tokenResponse = 로그인되어_있음("kid@kid", "kid");

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 석촌역.getId(), tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 잠실역, 석촌역));
        총_요금이_응답됨(response,  3450 - ((3450 - 350) * 0.5));
    }
}

