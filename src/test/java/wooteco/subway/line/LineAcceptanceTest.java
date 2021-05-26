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
import wooteco.subway.exception.dto.ExceptionResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.member.MemberAcceptanceTest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse downStation;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private static TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        tokenResponse = MemberAcceptanceTest.회원_로그인된_상태();
        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        downStation = 지하철역_등록되어_있음("광교역", tokenResponse);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-400", 강남역.getId(), downStation.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, tokenResponse);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithInvalidToken() {
        // when
        ExtractableResponse<Response> response = 유효하지_않은_토큰으로_지하철_노선_생성_요청(lineRequest1);

        // then
        잘못된_토큰으로_요청을_보냄(response);
    }

    @DisplayName("존재하지 않는 지하철 역으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithNotExistStationId() {
        // given
        LineRequest lineRequest = new LineRequest("3호선", "bg-red-100", 6L, 7L, 12);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest, tokenResponse);

        // then
        지하철_노선_생성_지하철역이_존재하지않아_실패됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, tokenResponse);

        // then
        지하철_노선_생성_이름중복으로_실패됨(response);
    }

    @DisplayName("지하철 노선을 생성요청에 유효하지 않은 값을 넣는다.")
    @Test
    void createLineWithInvalidValue() {
        // given
        LineRequest 노선이름_빈값 = new LineRequest("", "bg-red-600", 강남역.getId(), downStation.getId(), 10);
        LineRequest 노선색깔_빈값 = new LineRequest("신분당선", "", 강남역.getId(), downStation.getId(), 10);
        LineRequest 상행역_NULL = new LineRequest("신분당선", "bg-red-600", null, downStation.getId(), 10);
        LineRequest 상행역_음수 = new LineRequest("신분당선", "bg-red-600", -1L, downStation.getId(), 10);
        LineRequest 하행역_NULL = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), null, 10);
        LineRequest 하행역_음수 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), -1L, 10);
        LineRequest 구간거리_음수 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), -1);

        // when
        ExtractableResponse<Response> 노선이름_빈값_응답 = 지하철_노선_생성_요청(노선이름_빈값, tokenResponse);
        ExtractableResponse<Response> 노선색깔_빈값_응답 = 지하철_노선_생성_요청(노선색깔_빈값, tokenResponse);
        ExtractableResponse<Response> 상행역_NULL_응답 = 지하철_노선_생성_요청(상행역_NULL, tokenResponse);
        ExtractableResponse<Response> 하행역_NULL_응답 = 지하철_노선_생성_요청(하행역_NULL, tokenResponse);
        ExtractableResponse<Response> 상행역_음수_응답 = 지하철_노선_생성_요청(상행역_음수, tokenResponse);
        ExtractableResponse<Response> 하행역_음수_응답 = 지하철_노선_생성_요청(하행역_음수, tokenResponse);
        ExtractableResponse<Response> 구간거리_음수_응답 = 지하철_노선_생성_요청(구간거리_음수, tokenResponse);

        // then
        잘못된_입력값으로_요청을_보냄(노선이름_빈값_응답);
        잘못된_입력값으로_요청을_보냄(노선색깔_빈값_응답);
        잘못된_입력값으로_요청을_보냄(상행역_NULL_응답);
        잘못된_입력값으로_요청을_보냄(하행역_NULL_응답);
        지하철_역ID_음수요청됨(상행역_음수_응답);
        지하철_역ID_음수요청됨(하행역_음수_응답);
        유효하지_않은_거리값_요청됨(구간거리_음수_응답);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

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
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회한다.")
    @Test
    void findLineByNotExistId() {
        // given
        Long notExistId = 9999999999999L;

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(notExistId);

        // then
        지하철_노선이_존재하지_않음(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("8호선", "bg-red-600");

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineUpdateRequest);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 노선을 수정한다.")
    @Test
    void updateLineWithInvalidToken() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("8호선", "bg-red-600");

        // when
        ExtractableResponse<Response> response = 유효하지_않은_토큰으로_지하철_노선_수정_요청(lineResponse, lineUpdateRequest);

        // then
        잘못된_토큰으로_요청을_보냄(response);
    }

    @DisplayName("지하철 노선 이름을 이미 존재하는 이름으로 수정한다.")
    @Test
    void updateLineByDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(lineRequest1.getName(), "bg-red-600");

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse2, lineUpdateRequest);

        // then
        지하철_노선_이름_중복됨(response);
    }

    @DisplayName("지하철 노선 유효하지 않은 값으로 수정한다.")
    @Test
    void updateLineByInvalidValue() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);
        LineUpdateRequest 이름_Null = new LineUpdateRequest(null, "bg-red-600");
        LineUpdateRequest 이름_빈값 = new LineUpdateRequest("", "bg-red-600");
        LineUpdateRequest 색깔_Null = new LineUpdateRequest("8호선", null);
        LineUpdateRequest 색깔_빈값 = new LineUpdateRequest("8호선", "");

        // when
        ExtractableResponse<Response> 이름_Null_응답 = 지하철_노선_수정_요청(lineResponse, 이름_Null);
        ExtractableResponse<Response> 이름_빈값_응답 = 지하철_노선_수정_요청(lineResponse, 이름_빈값);
        ExtractableResponse<Response> 색깔_Null_응답 = 지하철_노선_수정_요청(lineResponse, 색깔_Null);
        ExtractableResponse<Response> 색깔_빈값_응답 = 지하철_노선_수정_요청(lineResponse, 색깔_빈값);

        // then
        잘못된_입력값으로_요청을_보냄(이름_Null_응답);
        잘못된_입력값으로_요청을_보냄(이름_빈값_응답);
        잘못된_입력값으로_요청을_보냄(색깔_Null_응답);
        잘못된_입력값으로_요청을_보냄(색깔_빈값_응답);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 노선을 제거한다.")
    @Test
    void deleteLineWithInvalidToken() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 유효하지_않은_토큰으로_지하철_노선_제거_요청(lineResponse);

        // then
        잘못된_토큰으로_요청을_보냄(response);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, TokenResponse tokenResponse) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest, tokenResponse);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest, TokenResponse tokenResponse) {
        return 지하철_노선_생성_요청(lineRequest, tokenResponse).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/lines")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/lines")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 유효하지_않은_토큰으로_지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "유효하지 않은 토큰이다요")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/lines")
                .then().log().all().
                        extract();
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

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineUpdateRequest params) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 유효하지_않은_토큰으로_지하철_노선_수정_요청(LineResponse response, LineUpdateRequest params) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "이상한 토큰")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/api/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 유효하지_않은_토큰으로_지하철_노선_제거_요청(LineResponse lineResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "유효하지 않은 토큰")
                .when().delete("/api/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_이름중복으로_실패됨(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    public static void 지하철_노선이_존재하지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(LineResponse::getId)
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

    public static void 지하철_노선_이름_중복됨(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("지하철 노선 이름이 이미 존재합니다");
    }

    public static void 지하철_역ID_음수요청됨(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("지하철역의 ID는 양수여야 합니다");
    }

    public static void 유효하지_않은_거리값_요청됨(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("거리는 1 이상의 숫자를 입력해주세요");
    }

    private void 지하철_노선_생성_지하철역이_존재하지않아_실패됨(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("존재하지 않는 지하철 역입니다");
    }
}
