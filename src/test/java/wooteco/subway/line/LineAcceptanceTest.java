package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.line.dto.CustomLineResponse;
import wooteco.subway.line.dto.LineMapResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 상봉역;
    private StationResponse 면목역;
    private StationResponse 용마산역;

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private LineRequest lineRequest3;

    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    public static ExtractableResponse<Response> 토큰과_함께_지하철_노선_조회_요청(LineResponse response) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/lines/{lineId}", response.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    public static ExtractableResponse<Response> 토큰과_함께_지하철_노선_수정_요청(LineResponse response, LineRequest params) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/api/lines/" + response.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 토큰과_함께_지하철_노선_제거_요청(LineResponse lineResponse) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().delete("/api/lines/" + lineResponse.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

    private static ExtractableResponse<Response> 토큰과_함께_지하철_노선_목록_조회_요청() {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);

        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().get("/api/lines")
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<Long> resultLineIds = response.jsonPath()
            .getList(".", CustomLineResponse.class)
            .stream()
            .map(CustomLineResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
            .map(LineResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
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

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        회원_생성을_요청(EMAIL, PASSWORD, 10);
        강남역 = 지하철역_등록되어_있음("강남역");
        광교역 = 지하철역_등록되어_있음("광교역");
        상봉역 = 지하철역_등록되어_있음("상봉역");
        면목역 = 지하철역_등록되어_있음("면목역");
        용마산역 = 지하철역_등록되어_있음("용마산역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 0);
        lineRequest2 = new LineRequest("7호선", "bg-green-600", 상봉역.getId(), 면목역.getId(), 15, 0);
        lineRequest3 = new LineRequest("5호선", "bg-purple-600", 상봉역.getId(), 용마산역.getId(), 4, 500);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 상,하행종점역과 총거리와 함께 조회한다.")
    @Test
    void findLinesWithStationsAndTotalDistance() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
        지하철_노선_목록_상하행종점역_일치함(response, Arrays.asList(lineResponse1, lineResponse2));
        지하철_노선_총_거리_일치함(response, Arrays.asList(lineRequest1, lineRequest2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철_노선_수정_요청(lineResponse, lineRequest2);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철_노선_제거_요청(lineResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("지하철 지도 전체 정보를 가져온다.")
    @Test
    void findLineMapResponses() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);
        지하철_노선_등록되어_있음(lineRequest2);
        지하철_노선_등록되어_있음(lineRequest3);

        // when
        ExtractableResponse<Response> response = 토근과_함께_지하철_전체_정보_요청();

        // then
        지하철_전체_정보_응답됨(response);
        지하철_전체_노선_정보_확인(response, Arrays.asList(lineRequest3, lineRequest2, lineRequest1));
    }

    private void 지하철_전체_노선_정보_확인(ExtractableResponse<Response> response, List<LineRequest> expectOrderRequests) {
        List<LineMapResponse> lineMapResponses = response.jsonPath().getList(".", LineMapResponse.class);

        for (int i = 0; i < lineMapResponses.size(); i++) {
            assertThat(lineMapResponses.get(i).getName()).isEqualTo(expectOrderRequests.get(i).getName());
            assertThat(lineMapResponses.get(i).getColor()).isEqualTo(expectOrderRequests.get(i).getColor());
            assertThat(lineMapResponses.get(i).getDistance()).isEqualTo(expectOrderRequests.get(i).getDistance());
        }
    }

    private void 지하철_전체_정보_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 토근과_함께_지하철_전체_정보_요청() {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);

        return RestAssured.given()
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().get("/api/lines/map")
            .then().log().all()
            .extract();
    }

    private void 지하철_노선_총_거리_일치함(ExtractableResponse<Response> response, List<LineRequest> lineRequests) {
        List<CustomLineResponse> customLineResponses = response.jsonPath().getList(".", CustomLineResponse.class);
        for (int i = 0; i < lineRequests.size(); i++) {
            CustomLineResponse customLineResponse = customLineResponses.get(i);
            LineRequest lineRequest = lineRequests.get(i);

            assertThat(customLineResponse.getDistance()).isEqualTo(lineRequest.getDistance());
        }
    }

    private void 지하철_노선_목록_상하행종점역_일치함(ExtractableResponse<Response> response, List<LineResponse> lineResponses) {
        List<CustomLineResponse> customLineResponses = response.jsonPath().getList(".", CustomLineResponse.class);
        for (int i = 0; i < lineResponses.size(); i++) {
            CustomLineResponse customLineResponse = customLineResponses.get(i);
            LineResponse lineResponse = lineResponses.get(i);
            int size = lineResponse.getStations().size();

            지하철_노선_상하행종점역_일치함(customLineResponse, lineResponse, size);
        }
    }

    private void 지하철_노선_상하행종점역_일치함(CustomLineResponse customLineResponse, LineResponse lineResponse, int size) {
        assertThat(customLineResponse.getStartStation())
            .usingRecursiveComparison()
            .isEqualTo(lineResponse.getStations().get(0));

        assertThat(customLineResponse.getEndStation())
            .usingRecursiveComparison()
            .isEqualTo(lineResponse.getStations().get(size - 1));
    }
}
