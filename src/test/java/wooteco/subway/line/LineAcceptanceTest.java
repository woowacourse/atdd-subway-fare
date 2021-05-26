package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.AuthAcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionResponse;
import wooteco.subway.line.exception.LineException;
import wooteco.subway.line.exception.SectionException;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.StationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 신분당선;
    private LineRequest 구신분당선;
    private LineRequest 일호선;

    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        tokenResponse = AuthAcceptanceTest.회원가입_토큰가져오기();

        // given
        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        광교역 = 지하철역_등록되어_있음("광교역", tokenResponse);

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        구신분당선 = new LineRequest("구신분당선", "bg-green-600", 강남역.getId(), 광교역.getId(), 15);
        일호선 = new LineRequest("1호선", "bg-blue-600", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선, tokenResponse);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-blue-600", 강남역.getId(), 광교역.getId(), 10), tokenResponse);

        // then
        에러_발생함(response, LineException.DUPLICATED_LINE_NAME_EXCEPTION);
    }

    @DisplayName("2-10의 길이와 한글, 숫자가 아닌 이름으로 지하철 노선을 생성하면 에러가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "a", "공백은 불가", "열글자가넘어도절대절대안되요", "!@$!^"})
    void createLineWithInvalidName(String name) {
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest(name, "bg-red-600", 강남역.getId(), 광교역.getId(), 10), tokenResponse);

        에러_발생함(response, LineException.INVALID_LINE_EXCEPTION_EXCEPTION);
    }

    @DisplayName("기존에 존재하는 지하철 노선 색깔로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateColor() {
        // given
        지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선2", "bg-red-600", 강남역.getId(), 광교역.getId(), 10), tokenResponse);

        // then
        에러_발생함(response, LineException.DUPLICATED_LINE_COLOR_EXCEPTION);
    }

    @DisplayName("노선 생성 시 거리가 0이하일 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void createLineWithInvalidDistance(int distance) {

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선2", "bg-red-600", 강남역.getId(), 광교역.getId(), distance), tokenResponse);

        // then
        에러_발생함(response, SectionException.INVALID_SECTION_DISTANCE_EXCEPTION);
    }

    @DisplayName("존재하지 않는 역으로 노선을 생성할 수 없다.")
    @Test
    void createLineWithNotExistStation() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-blue-600", null, null, 10), tokenResponse);

        // then
        에러_발생함(response, StationException.NOT_FOUND_STATION_EXCEPTION);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(신분당선, tokenResponse);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(구신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청(tokenResponse);

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse, tokenResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회할 수 없다.")
    @Test
    void getLineWithNotExistLine() {
        // given
        지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(new LineResponse(3L, "존재하지않는노선", "RED", new ArrayList<>(), new ArrayList<>()), tokenResponse);

        // then
        에러_발생함(response, LineException.NOT_EXIST_LINE_EXCEPTION);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, 구신분당선, tokenResponse);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정한다.")
    @Test
    void updateLineWithNotExistLine() {
        // given
        지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(new LineResponse(3L, "존재하지않는노선", "RED", new ArrayList<>(), new ArrayList<>()), 구신분당선, tokenResponse);

        // then
        에러_발생함(response, LineException.NOT_EXIST_LINE_EXCEPTION);
    }

    @DisplayName("중복된 노선 이름으로 수정할 수 없다.")
    @Test
    void updateLineWithDuplicatedName() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선, tokenResponse);
        지하철_노선_등록되어_있음(일호선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse,
                new LineRequest("1호선", lineResponse.getColor(), 강남역.getId(), 광교역.getId(), 15), tokenResponse);

        // then
        에러_발생함(response, LineException.DUPLICATED_LINE_NAME_EXCEPTION);
    }

    @DisplayName("중복된 노선 색상으로 수정할 수 없다.")
    @Test
    void updateLineWithDuplicatedColor() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선, tokenResponse);
        지하철_노선_등록되어_있음(일호선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse,
                new LineRequest(lineResponse.getName(), "bg-blue-600", 강남역.getId(), 광교역.getId(), 15), tokenResponse);

        // then
        에러_발생함(response, LineException.DUPLICATED_LINE_COLOR_EXCEPTION);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse, tokenResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 제거한다.")
    @Test
    void deleteLineWithNotExistLine() {
        // given
        지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(new LineResponse(3L, "존재하지않는노선", "RED", new ArrayList<>(), new ArrayList<>()), tokenResponse);

        // then
        에러_발생함(response, LineException.NOT_EXIST_LINE_EXCEPTION);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, TokenResponse tokenResponse) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest, tokenResponse);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest, TokenResponse tokenResponse) {
        return 지하철_노선_생성_요청(lineRequest, tokenResponse).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineRequest params, TokenResponse tokenResponse) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
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
