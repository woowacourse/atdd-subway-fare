package wooteco.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_생성_요청_withToken;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("사용자 권한 (조회 이외의 기능 사용불가 401) - 역 생성")
    @Test
    void tokenValidation_create() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자 권한 (조회 이외의 기능 사용불가 401) - 역 수정")
    @Test
    void tokenValidation_update() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        StationResponse stationResponse = 지하철역_등록되어_있음_withToken(사용자, 강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse, "역삼역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자 권한 (조회 이외의 기능 사용불가 401) - 역 삭제")
    @Test
    void tokenValidation_delete() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        StationResponse stationResponse = 지하철역_등록되어_있음_withToken(사용자, 강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("역 생성 - 지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청_withToken(사용자, 강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("역 생성 - 기존에 존재하는 지하철역 이름으로 역을 생성할 수 없다. (400)")
    @Test
    void createStationWithDuplicateName() {
        //given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        지하철역_등록되어_있음_withToken(사용자, 강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청_withToken(사용자, 강남역);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("목록 조회 - 지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        StationResponse stationResponse1 = 지하철역_등록되어_있음_withToken(사용자, 강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음_withToken(사용자, 역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("이름 수정 - 지하철역 이름을 수정한다.")
    @Test
    void changeStationName() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        StationResponse stationResponse = 지하철역_등록되어_있음_withToken(사용자, 강남역);
        String newStationName = "수정된강남역";

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청_withToken(사용자, stationResponse, newStationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("이름 수정 - 이미 존재하는 지하철 역으로 수정할 수 없다. (400)")
    @Test
    void changeDuplicateStationName() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        StationResponse stationResponse = 지하철역_등록되어_있음_withToken(사용자, 강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음_withToken(사용자, 역삼역);
        String newStationName = "역삼역";

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청_withToken(사용자, stationResponse, newStationName);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @DisplayName("역 삭제 - 지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        StationResponse stationResponse = 지하철역_등록되어_있음_withToken(사용자, 강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청_withToken(사용자, stationResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("역 삭제 - 노선에 포함된 지하철역은 삭제할 수 없다. (400)")
    @Test
    @Ignore
    void deleteStationException() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        StationResponse stationResponse1 = 지하철역_등록되어_있음_withToken(사용자, 강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음_withToken(사용자, 역삼역);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", stationResponse1.getId(), stationResponse2.getId(), 10);
        ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청_withToken(사용자, lineRequest);
        지하철_노선_생성됨(createLineResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청_withToken(사용자, stationResponse1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static StationResponse 지하철역_등록되어_있음_withToken(TokenResponse tokenResponse, String name) {
        return 지하철역_생성_요청_withToken(tokenResponse, name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청_withToken(TokenResponse tokenResponse, String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철역_수정_요청(StationResponse stationResponse, String newStationName) {
        StationRequest stationRequest = new StationRequest(newStationName);

        return RestAssured
            .given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철역_수정_요청_withToken(TokenResponse tokenResponse, StationResponse stationResponse, String newStationName) {
        StationRequest stationRequest = new StationRequest(newStationName);

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청_withToken(TokenResponse tokenResponse, StationResponse stationResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete("/api/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<StationResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
