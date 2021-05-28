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

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음_추가요금;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 고속터미널역;
    private StationResponse 선릉역;

    public static ExtractableResponse<Response> 거리_경로_조회_요청_회원(TokenResponse tokenResponse, long source, long target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청_비회원(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 적절한_경로_응답됨(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(toList());

        List<Long> expectedPathIds = expectedPath.stream()
                .map(StationResponse::getId)
                .collect(toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    public static void 총_요금이_응답됨(ExtractableResponse<Response> response, int totalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDefaultFare()).isEqualTo(totalFare);
    }

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
        고속터미널역 = 지하철역_등록되어_있음("고속터미널역");
        선릉역 = 지하철역_등록되어_있음("선릉역");

        신분당선 = 지하철_노선_등록되어_있음_추가요금("신분당선", "bg-red-600", 900, 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-orange-600", 교대역, 양재역, 5);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        지하철_구간_등록되어_있음(신분당선, 고속터미널역, 강남역, 5);
        지하철_구간_등록되어_있음(신분당선, 양재역, 선릉역, 5);
    }

    @DisplayName("경로 조회 - 두 역의 최단 거리 경로에 따른 거리와 요금을 조회한다. (노선 추가 요금 있음, 회원)")
    @Test
    void findFareByLineWithExtraFareByMember() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 거리_경로_조회_요청_회원(사용자, 3L, 6L);

        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역, 선릉역));
        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 900);
    }

    @DisplayName("경로 조회 - 두 역의 최단 거리 경로에 따른 거리와 요금을 조회한다. (노선 추가 요금 없음, 회원)")
    @Test
    void findFareByLineWithoutExtraFareByMember() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 거리_경로_조회_요청_회원(사용자, 3L, 2L);

        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 450);
    }

    @DisplayName("경로 조회 - 두 역의 최단 거리 경로에 따른 거리와 요금을 조회한다. (노선 추가 요금 있음, 비회원)")
    @Test
    void findFareByLineWithExtraFareByNonMember() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청_비회원(3L, 6L);

        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역, 선릉역));
        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("경로 조회 - 두 역의 최단 거리 경로에 따른 거리와 요금을 조회한다. (노선 추가 요금 없음, 비회원)")
    @Test
    void findFareByLineWithoutExtraFareByNonMember() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청_비회원(3L, 2L);

        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1250);
    }
}
