package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.line.dto.LineInfoResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.*;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse downStation;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private static String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);
        accessToken = tokenResponse.getAccessToken();

        // given
        강남역 = 지하철역_등록되어_있음("강남역", accessToken);
        downStation = 지하철역_등록되어_있음("광교역", accessToken);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(accessToken, lineRequest1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithoutToken() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("accessToken", lineRequest1);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("INVALID_TOKEN");
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(accessToken, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(accessToken, lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("옳지 않은 이름으로 지하철 노선을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"!!:color:1:2:10", "name: :1:2:10", ":color:1:2:1"}, delimiter = ':')
    void createInvalidStation(String name, String color, Long upStationId, Long downStationId, int distance) {
        // given
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(accessToken, lineRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("INVALID_NAME");
    }

    @DisplayName("옳지 않은 거리로 지하철 노선을 생성한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void createInvalidDistance(int distance) {
        // given
        LineRequest lineRequest = new LineRequest("name", "color", 1L, 3L, distance);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(accessToken, lineRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("INVALID_DISTANCE");
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(accessToken, lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(accessToken, lineRequest2);

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
        LineResponse lineResponse = 지하철_노선_등록되어_있음(accessToken, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(accessToken, lineResponse.getId());

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회한다.")
    @Test
    void getNotExistLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(accessToken, 11L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("NO_SUCH_LINE");
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(accessToken, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse.getId(), lineRequest2);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정한다.")
    @Test
    void updateNotExistLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(11L, lineRequest2);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("NO_SUCH_LINE");
    }

    @DisplayName("지하철 노선을 제거한다. 구간도 함께 제거된다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(accessToken, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse.getId());

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 삭제한다.")
    @Test
    void deleteNotExistLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(11L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("NO_SUCH_LINE");
    }

    public static LineResponse 지하철_노선_등록되어_있음(String accessToken, String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return 지하철_노선_등록되어_있음(accessToken, lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String accessToken, LineRequest lineRequest) {
        return 지하철_노선_생성_요청(accessToken, lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String accessToken, LineRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/lines")
                .then().log().all().
                        extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String accessToken, Long lineId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long lineId, LineRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(Long lineId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/api/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("DUPLICATED_LINE_NAME");
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

        List<Long> resultLineIds = response.jsonPath().getList(".", LineInfoResponse.class).stream()
                .map(LineInfoResponse::getId)
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
