package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
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
import wooteco.subway.line.exception.SubwayLineException;
import wooteco.subway.line.exception.SubwaySectionException;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.SubwayStationException;

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

        // given
        tokenResponse = AuthAcceptanceTest.회원가입_토큰가져오기();
        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        downStation = 지하철역_등록되어_있음("광교역", tokenResponse);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-green-600", 강남역.getId(), downStation.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, tokenResponse);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성하면 에러가 발생한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        LineRequest lineRequest = new LineRequest("신분당선", "bg-green-600", 강남역.getId(), downStation.getId(), 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest, tokenResponse);

        // then
        에러가_발생한다(response, SubwayLineException.DUPLICATE_LINE_NAME_EXCEPTION);
    }

    @DisplayName("2글자이상 10글자이하 한글,숫자가 아닌 이름으로 지하철 노선을 생성하면 에러가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "    ", "abc", "일", "공백이 들어가면 안된다", "노선의이름은열글자가넘으면안된다", "특수문자안됨!"})
    void createLineWithInvalidName(String value) {
        // given
        LineRequest lineRequest = new LineRequest(value, "bg-red-600", 강남역.getId(), downStation.getId(), 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest, tokenResponse);

        // then
        에러가_발생한다(response, SubwayLineException.INVALID_LINE_EXCEPTION);
    }

    @DisplayName("기존에 존재하는 지하철 노선 색상으로 지하철 노선을 생성하면 에러가 발생한다.")
    @Test
    void createLineWithDuplicateColor() {
        // given
        지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        LineRequest lineRequest = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 15);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest, tokenResponse);

        // then
        에러가_발생한다(response, SubwayLineException.DUPLICATE_LINE_COLOR_EXCEPTION);
    }

    @DisplayName("0 이하의 구간 거리로 지하철 노선을 생성하면 에러가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void createLineWithInvalidName(int value) {
        // given
        LineRequest lineRequest = new LineRequest("신림역", "bg-red-600", 강남역.getId(), downStation.getId(), value);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest, tokenResponse);

        // then
        에러가_발생한다(response, SubwaySectionException.INVALID_SECTION_DISTANCE_EXCEPTION);
    }

    @DisplayName("존재하지 않는 역으로 지하철 노선을 생성하면 에러가 발생한다.")
    @Test
    void createLineWithNotExistStation() {
        // given
        LineRequest lineRequest = new LineRequest("구신분당선", "bg-red-600", 3L, 4L, 15);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest, tokenResponse);

        // then
        에러가_발생한다(response, SubwayStationException.NOT_EXIST_STATION_EXCEPTION);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2, tokenResponse);

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
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse, tokenResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회하면 에러가 발생한다.")
    @Test
    void getLineWithNotExistLine() {
        // given
        LineResponse lineResponse = new LineResponse(1L, "2호선", "bg-green-600", null);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse, tokenResponse);

        // then
        에러가_발생한다(response, SubwayLineException.NOT_EXIST_LINE_EXCEPTION);
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

    @DisplayName("존재하는 지하철 노선이름으로 수정하면 에러가 발생한다.")
    @Test
    void updateLineWithDuplicateName() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        지하철_노선_등록되어_있음(lineRequest2, tokenResponse);
        LineRequest lineRequest = new LineRequest("구신분당선", "bg-yellow-600", 강남역.getId(), downStation.getId(), 15);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest, tokenResponse);

        // then
        에러가_발생한다(response, SubwayLineException.DUPLICATE_LINE_NAME_EXCEPTION);
    }

    @DisplayName("존재하는 지하철 노선 색상으로 수정하면 에러가 발생한다.")
    @Test
    void updateLineWithDuplicateColor() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);
        지하철_노선_등록되어_있음(lineRequest2, tokenResponse);
        LineRequest lineRequest = new LineRequest("신림역", "bg-green-600", 강남역.getId(), downStation.getId(), 15);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest, tokenResponse);

        // then
        에러가_발생한다(response, SubwayLineException.DUPLICATE_LINE_COLOR_EXCEPTION);
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정하면 에러가 발생한다.")
    @Test
    void updateLineWithNotExistLine() {
        // given
        LineResponse lineResponse = new LineResponse(1L, "2호선", "bg-green-600", null);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2, tokenResponse);

        // then
        에러가_발생한다(response, SubwayLineException.NOT_EXIST_LINE_EXCEPTION);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse, tokenResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 제거하면 에러가 발생한다..")
    @Test
    void deleteLineWithNotExistLine() {
        // given
        LineResponse lineResponse = new LineResponse(1L, "2호선", "bg-green-600", null);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse, tokenResponse);

        // then
        에러가_발생한다(response, SubwayLineException.NOT_EXIST_LINE_EXCEPTION);
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
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

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", response.getId())
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

    private ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineRequest params, TokenResponse tokenResponse) {

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/lines/" + response.getId())
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse, TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete("/lines/" + lineResponse.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
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
}
