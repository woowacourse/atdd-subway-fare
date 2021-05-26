package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.dto.LineUpdateResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음_withToken;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private TokenResponse 사용자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        강남역 = 지하철역_등록되어_있음_withToken(사용자, "강남역");
        광교역 = 지하철역_등록되어_있음_withToken(사용자, "광교역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("사용자 권한 (조회 이외의 기능 사용불가 401) - 노선 생성")
    @Test
    void tokenValidation_create() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자 권한 (조회 이외의 기능 사용 불가 401) - 노선 수정")
    @Test
    void tokenValidation_update() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);

        // when
        LineUpdateRequest updateRequest1 = new LineUpdateRequest(lineRequest2.getName(), lineRequest2.getColor());
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, updateRequest1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("사용자 권한 (조회 이외의 기능 사용 불가 401) - 노선 삭제")
    @Test
    void tokenValidation_delete() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("노선 생성 - 지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청_withToken(사용자, lineRequest1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("노선 생성 - 기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성할 수 없다. (400)")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청_withToken(사용자, lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("노선 생성 - 존재하지 않는 역으로 노선을 생성할 수 없다. (404)")
    @Test
    void createLineWithNonExistsStation() {
        // given
        지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);

        // when
        LineRequest lineRequest = new LineRequest("3호선", "bg-blue-500", -1L, 강남역.getId(), 5);
        ExtractableResponse<Response> response = 지하철_노선_생성_요청_withToken(사용자, lineRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("노선 생성 - 거리가 양수가 아닐 경우 노선을 생성할 수 없다. (400)")
    @Test
    void createLineWithMinusDistance() {
        // given
        지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);

        // when
        LineRequest lineRequest = new LineRequest("3호선", "bg-blue-500", 광교역.getId(), 강남역.getId(), -3);
        ExtractableResponse<Response> response = 지하철_노선_생성_요청_withToken(사용자, lineRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("목록 조회 - 지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("조회 - 지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("노선 수정 - 지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);

        // when
        LineUpdateRequest updateRequest = new LineUpdateRequest(lineRequest2.getName(), lineRequest2.getColor());
        ExtractableResponse<Response> response = 지하철_노선_수정_요청_withToken(사용자, lineResponse, updateRequest);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("노선 수정 - 이미 존재하는 이름이면 노선을 생성할 수 없다. (400)")
    @Test
    void updateLineWithDuplicate() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest2);

        // when
        LineUpdateRequest updateRequest = new LineUpdateRequest(lineResponse2.getName(), lineResponse2.getColor());
        ExtractableResponse<Response> response = 지하철_노선_수정_요청_withToken(사용자, lineResponse1, updateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선 제거 - 지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청_withToken(사용자, lineResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static LineResponse 지하철_노선_등록되어_있음_withToken(TokenResponse tokenResponse, LineRequest lineRequest) {
        return 지하철_노선_생성_요청_withToken(tokenResponse, lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/lines")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청_withToken(TokenResponse tokenResponse, LineRequest params) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/api/lines")
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineUpdateRequest params) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/api/lines/" + response.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청_withToken(TokenResponse tokenResponse, LineResponse response, LineUpdateRequest params) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청_withToken(TokenResponse tokenResponse, LineResponse lineResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete("/api/lines/" + lineResponse.getId())
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

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
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
