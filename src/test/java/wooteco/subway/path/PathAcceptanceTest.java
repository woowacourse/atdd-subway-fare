package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

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
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 토큰과_함께_거리_경로_조회_요청(long source, long target) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 토큰과_함께_거리_경로_조회_요청(String email, String password, long source, long target) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(email, password)).jsonPath().get(ACCESS_TOKEN);
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
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

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/api/members")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_후_토큰_발급(TokenRequest tokenRequest) {
        return RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when()
            .post("/api/login/token")
            .then().extract();
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 토큰과_함께_지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 토큰과_함께_지하철역_생성_요청(String name) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
            .given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().post("/api/stations")
            .then().log().all()
            .extract();
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 토큰과_함께_지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 토큰과_함께_지하철_노선_생성_요청(LineRequest lineRequest) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);

        return RestAssured.given()
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(lineRequest)
            .when().post("/api/lines")
            .then().log().all()
            .extract();
    }

    public static void 지하철_구간_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        토큰과_함께_지하철_구간_생성_요청(lineResponse, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 토큰과_함께_지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation,
        int distance) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(sectionRequest)
            .when().post("/api/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }

    /**
     * 교대역    --- *2호선* ---   강남역 |                        | *3호선*                   *신분당선* |                        | 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(EMAIL, PASSWORD, 20);
        강남역 = 지하철역_등록되어_있음("강남역"); // 1
        양재역 = 지하철역_등록되어_있음("양재역"); // 2
        교대역 = 지하철역_등록되어_있음("교대역"); // 3
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역"); // 4

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 0));
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 0));
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 0));

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("회원/비회원이 최단 거리 경로를 조회한다.")
    @Test
    void findPathByMemberAndGuest() {
        // given
        String babyEmail = "baby@baby.com";
        String childEmail = "child@child.com";
        String teenagerEmail = "teenager@teenager.com";
        String adultEmail = "adult@adult.com";
        String password = "1234";
        회원_생성을_요청(babyEmail, password, 5);
        회원_생성을_요청(childEmail, password, 7);
        회원_생성을_요청(teenagerEmail, password, 15);
        회원_생성을_요청(adultEmail, password, 20);

        // when
        ExtractableResponse<Response> babyResponse = 토큰과_함께_거리_경로_조회_요청(babyEmail, password, 3L, 2L);
        ExtractableResponse<Response> childResponse = 토큰과_함께_거리_경로_조회_요청(childEmail, password, 3L, 2L);
        ExtractableResponse<Response> teenagerResponse = 토큰과_함께_거리_경로_조회_요청(teenagerEmail, password, 3L, 2L);
        ExtractableResponse<Response> adultResponse = 토큰과_함께_거리_경로_조회_요청(adultEmail, password, 3L, 2L);
        ExtractableResponse<Response> guestResponse = 거리_경로_조회_요청(3L, 2L);

        // then
        적절한_경로_응답됨(babyResponse, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(babyResponse, 5);
        총_금액이_응답됨(babyResponse, 0);

        적절한_경로_응답됨(childResponse, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(childResponse, 5);
        총_금액이_응답됨(childResponse, (int) ((1250 - 350) * (1 - 0.5)));

        적절한_경로_응답됨(teenagerResponse, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(teenagerResponse, 5);
        총_금액이_응답됨(teenagerResponse, (int) ((1250 - 350) * (1 - 0.2)));

        적절한_경로_응답됨(adultResponse, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(adultResponse, 5);
        총_금액이_응답됨(adultResponse, 1250);
    }

    @DisplayName("연령별 할인정책이 포함된 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceAndAge() {
        // given
        String babyEmail = "baby@baby.com";
        String childEmail = "child@child.com";
        String teenagerEmail = "teenager@teenager.com";
        String adultEmail = "adult@adult.com";
        String password = "1234";
        회원_생성을_요청(babyEmail, password, 5);
        회원_생성을_요청(childEmail, password, 7);
        회원_생성을_요청(teenagerEmail, password, 15);
        회원_생성을_요청(adultEmail, password, 20);

        // when
        ExtractableResponse<Response> babyResponse = 토큰과_함께_거리_경로_조회_요청(babyEmail, password, 3L, 2L);
        ExtractableResponse<Response> childResponse = 토큰과_함께_거리_경로_조회_요청(childEmail, password, 3L, 2L);
        ExtractableResponse<Response> teenagerResponse = 토큰과_함께_거리_경로_조회_요청(teenagerEmail, password, 3L, 2L);
        ExtractableResponse<Response> adultResponse = 토큰과_함께_거리_경로_조회_요청(adultEmail, password, 3L, 2L);

        // then
        적절한_경로_응답됨(babyResponse, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(babyResponse, 5);
        총_금액이_응답됨(babyResponse, 0);

        적절한_경로_응답됨(childResponse, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(childResponse, 5);
        총_금액이_응답됨(childResponse, (int) ((1250 - 350) * (1 - 0.5)));

        적절한_경로_응답됨(teenagerResponse, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(teenagerResponse, 5);
        총_금액이_응답됨(teenagerResponse, (int) ((1250 - 350) * (1 - 0.2)));

        적절한_경로_응답됨(adultResponse, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(adultResponse, 5);
        총_금액이_응답됨(adultResponse, 1250);
    }

    @DisplayName("노선별 추가요금이 포함된 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceAndExtraFare() {
        // given
        지하철_노선_등록되어_있음(new LineRequest("테스트선", "bg-red-600", 강남역.getId(), 양재역.getId(), 7, 9_000));

        // when
        ExtractableResponse<Response> response1 = 토큰과_함께_거리_경로_조회_요청(1L, 2L);

        // then
        적절한_경로_응답됨(response1, Lists.newArrayList(강남역, 양재역));
        총_거리가_응답됨(response1, 7);
        총_금액이_응답됨(response1, 1250 + 9_000);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response1 = 토큰과_함께_거리_경로_조회_요청(3L, 2L);
        ExtractableResponse<Response> response2 = 토큰과_함께_거리_경로_조회_요청(1L, 4L);

        // then
        적절한_경로_응답됨(response1, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response1, 5);
        총_금액이_응답됨(response1, 1250);

        적절한_경로_응답됨(response2, Lists.newArrayList(강남역, 양재역, 남부터미널역));
        총_거리가_응답됨(response2, 12);
        총_금액이_응답됨(response2, 1250 + 100);
    }

    private void 총_금액이_응답됨(ExtractableResponse<Response> response, int expectFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(expectFare);
    }
}
