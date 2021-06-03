package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.인증_실패됨;
import static wooteco.subway.auth.AuthAcceptanceTest.회원가입_후_로그인;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

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
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse downStation;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenResponse = 회원가입_후_로그인();

        // given
        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        downStation = 지하철역_등록되어_있음("광교역", tokenResponse);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 0, 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-orange-600", 강남역.getId(), downStation.getId(), 0,
                15);
    }

    @DisplayName("유효한 토큰으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithValidToken() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, tokenResponse);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithInvalidToken() {
        // given
        TokenResponse invalidTokenResponse = new TokenResponse("invalid token");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, invalidTokenResponse);

        // then
        인증_실패됨(response);
    }

    @DisplayName("토큰 없이 지하철 노선을 생성한다.")
    @Test
    void createLineWithoutToken() {
        // given

        // when
        ExtractableResponse<Response> response = 토큰없이_지하철_노선_생성_요청(lineRequest1);

        // then
        인증_실패됨(response);
    }

    @DisplayName("존재하지 않는 역으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithNoneExistingStations() {
        // given
        LineRequest 잘못된요청 = new LineRequest(
                "호남선",
                "bg-pink-600",
                100L,
                101L,
                0,
                10
        );

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(잘못된요청, tokenResponse);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        LineRequest duplicatedLineNameRequest =
                new LineRequest("신분당선", "bg-red-601", 강남역.getId(), downStation.getId(), 0, 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(duplicatedLineNameRequest,
                tokenResponse);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 색상으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateColor() {
        // given
        지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        LineRequest duplicatedLineColorRequest =
                new LineRequest("백기선", "bg-red-600", 강남역.getId(), downStation.getId(), 0, 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(duplicatedLineColorRequest,
                tokenResponse);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2, tokenResponse);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 노선을 수정한다.")
    @Test
    void updateLineWithInvalidToken() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        TokenResponse invalidTokenResponse = new TokenResponse("invalid token");

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2, invalidTokenResponse);

        // then
        인증_실패됨(response);
    }

    @DisplayName("토큰 없이 지하철 노선을 수정한다.")
    @Test
    void updateLineWithoutToken() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 토큰없이_지하철_노선_수정_요청(lineResponse, lineRequest2);

        // then
        인증_실패됨(response);
    }


    @DisplayName("유효한 토큰으로 지하철 노선을 제거한다.")
    @Test
    void deleteLineWithValidToken() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse, tokenResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 노선을 제거한다.")
    @Test
    void deleteLineWithInvalidToken() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        TokenResponse invalidToken = new TokenResponse("Invalid Token");

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse, invalidToken);

        // then
        인증_실패됨(response);
    }

    @DisplayName("토큰없이 지하철 노선을 제거한다.")
    @Test
    void deleteLineWithoutToken() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 토큰없이_지하철_노선_제거_요청(lineResponse);

        // then
        인증_실패됨(response);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, TokenResponse tokenResponse) {
        return 지하철_노선_등록되어_있음(name, color, upStation, downStation, 0, distance, tokenResponse);
//        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
//        return 지하철_노선_등록되어_있음(lineRequest, tokenResponse);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int extraFare, int distance, TokenResponse tokenResponse) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), extraFare, distance);
        return 지하철_노선_등록되어_있음(lineRequest, tokenResponse);
    }


    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest,
            TokenResponse tokenResponse) {
        return 지하철_노선_생성_요청(lineRequest, tokenResponse).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params,
            TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰없이_지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response,
            LineRequest params, TokenResponse tokenResponse) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰없이_지하철_노선_수정_요청(LineResponse response,
            LineRequest params) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse,
            TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰없이_지하철_노선_제거_요청(LineResponse lineResponse) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }


    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response,
            LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response,
            List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
