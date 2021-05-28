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
import wooteco.subway.member.MemberAcceptanceTest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 잠실역;
    private StationResponse 강북역;
    private StationResponse 구의역;
    private StationResponse 건대입구역;
    private static TokenResponse tokenResponse;

    /**
     * 건대입구
     * ㅣ
     * *3호선*
     * ㅣ
     * 잠실역-- *4호선* --교대역    --- *2호선* ---   강남역 -- *2호선* -- 강북역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역 -- *3호선* - 구의역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenResponse = MemberAcceptanceTest.회원_로그인된_상태();

        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        양재역 = 지하철역_등록되어_있음("양재역", tokenResponse);
        교대역 = 지하철역_등록되어_있음("교대역", tokenResponse);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역", tokenResponse);
        잠실역 = 지하철역_등록되어_있음("잠실역", tokenResponse);
        강북역 = 지하철역_등록되어_있음("강북역", tokenResponse);
        구의역 = 지하철역_등록되어_있음("구의역", tokenResponse);
        건대입구역 = 지하철역_등록되어_있음("건대입구역", tokenResponse);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 100, 강남역, 양재역, 10, tokenResponse);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-500", 200, 교대역, 강남역, 10, tokenResponse);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-400", 300, 교대역, 양재역, 5, tokenResponse);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-300", 400, 잠실역, 교대역, 58, tokenResponse);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3, tokenResponse);
        지하철_구간_등록되어_있음(이호선, 강남역, 강북역, 49, tokenResponse);
        지하철_구간_등록되어_있음(삼호선, 건대입구역, 교대역, 50, tokenResponse);
        지하철_구간_등록되어_있음(삼호선, 양재역, 구의역, 59, tokenResponse);
    }

    @DisplayName("로그인 되지 않은 상태로 경로를 조회한다.")
    @Test
    void findPathByDistanceWithOutLogin() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5, 1550);
    }

    @DisplayName("성인 회원이 두 역의 최단 거리 경로와 요금을 조회한다.")
    @Test
    void findPathByDistance() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청("adult@email.com", "1234", 20);
        회원_생성됨(createResponse);

        TokenResponse tokenResponse = 로그인되어_있음("adult@email.com", "1234");

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5, 1550);
    }

    @DisplayName("청소년 회원이 두 역의 최단 거리 경로와 요금을 조회한다.")
    @Test
    void findPathByDistanceWithTeenagerMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청("teenager@email.com", "1234", 18);
        회원_생성됨(createResponse);

        TokenResponse tokenResponse = 로그인되어_있음("teenager@email.com", "1234");

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5, 960);
    }

    @DisplayName("어린이 회원이 두 역의 최단 거리 경로와 요금을 조회한다.")
    @Test
    void findPathByDistanceWithChildMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청("child@email.com", "1234", 12);
        회원_생성됨(createResponse);

        TokenResponse tokenResponse = 로그인되어_있음("child@email.com", "1234");

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5, 600);
    }

    @DisplayName("유아 회원이 두 역의 최단 거리 경로와 요금을 조회한다.")
    @Test
    void findPathByDistanceWithBabyMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청("baby@email.com", "1234", 5);
        회원_생성됨(createResponse);

        TokenResponse tokenResponse = 로그인되어_있음("baby@email.com", "1234");

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5, 0);
    }

    @DisplayName("10km 초과 ~ 40km 이하일 시 5km마다 100원 추가 요금 발생한다.")
    @Test
    void findPathByDistanceAndFirstBoundFare() {
        //when
        ExtractableResponse<Response> response1 = 거리_경로_조회_요청(4L, 1L, tokenResponse);
        ExtractableResponse<Response> response2 = 거리_경로_조회_요청(1L, 6L, tokenResponse);
        ExtractableResponse<Response> response3 = 거리_경로_조회_요청(8L, 3L, tokenResponse);

        //then
        적절한_경로_응답됨(response1, Lists.newArrayList(남부터미널역, 양재역, 강남역));
        총_거리가_응답됨(response1, 12, 1650);

        적절한_경로_응답됨(response2, Lists.newArrayList(강남역, 강북역));
        총_거리가_응답됨(response2, 49, 2250);

        적절한_경로_응답됨(response3, Lists.newArrayList(건대입구역, 교대역));
        총_거리가_응답됨(response3, 50, 2350);
    }

    @DisplayName("50km 초과 시 8km마다 100원 추가 요금 발생한다.")
    @Test
    void findPathByDistanceAndSecondBoundFare() {
        //when
        ExtractableResponse<Response> response1 = 거리_경로_조회_요청(5L, 3L, tokenResponse);

        ExtractableResponse<Response> response2 = 거리_경로_조회_요청(2L, 7L, tokenResponse);

        //then
        적절한_경로_응답됨(response1, Lists.newArrayList(잠실역, 교대역));
        총_거리가_응답됨(response1, 58, 2550);

        적절한_경로_응답됨(response2, Lists.newArrayList(양재역, 구의역));
        총_거리가_응답됨(response2, 59, 2550);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/api/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(Long source, Long target, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/api/paths")
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

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance, int totalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
        assertThat(pathResponse.getDefaultFare()).isEqualTo(totalFare);
    }
}
