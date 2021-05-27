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
import wooteco.subway.ErrorResponse;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
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

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        downStation = 지하철역_등록되어_있음("광교역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 10, 900);
        lineRequest2 = new LineRequest("구신분당선", "bg-pink-600", 강남역.getId(), downStation.getId(), 15, 1200);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성할 수 없다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        String lineName = "신분당선";
        LineRequest lineRequest = new LineRequest(lineName, "bg-red-600", 강남역.getId(), downStation.getId(), 10, 900);
        지하철_노선_등록되어_있음(lineRequest);

        // when
        LineRequest lineRequestWithSameName = new LineRequest(lineName, "bg-red-600", 강남역.getId(), downStation.getId(), 10, 900);
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequestWithSameName);

        // then
        지하철_노선_이름이_중복되어_생성_실패됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 색상으로 지하철 노선을 생성할 수 없다.")
    @Test
    void createLineWithDuplicateColor() {
        // given
        String lineColor = "bg-red-600";
        LineRequest lineRequest = new LineRequest("신분당선", lineColor, 강남역.getId(), downStation.getId(), 10, 900);
        지하철_노선_등록되어_있음(lineRequest);

        // when
        LineRequest lineRequestWithSameColor = new LineRequest("3호선", lineColor, 강남역.getId(), downStation.getId(), 10, 900);
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequestWithSameColor);

        // then
        지하철_노선_색상이_중복되어_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 등록 시 이름은 2자 이상 한글/숫자만 가능하며, 공백은 허용하지 않는다.")
    @Test
    void createLineWithInvalidName() {
        // given
        String invalidCase1 = "역";
        String invalidCase2 = "line";
        String invalidCase3 = "       ";
        String invalidCase4 = "$#*^";

        // when
        ExtractableResponse<Response> response1 = 지하철_노선_생성_요청(new LineRequest(invalidCase1, "bg-red-600", 강남역.getId(), downStation.getId(), 10));
        ExtractableResponse<Response> response2 = 지하철_노선_생성_요청(new LineRequest(invalidCase2, "bg-red-700", 강남역.getId(), downStation.getId(), 10));
        ExtractableResponse<Response> response3 = 지하철_노선_생성_요청(new LineRequest(invalidCase3, "bg-red-800", 강남역.getId(), downStation.getId(), 10));
        ExtractableResponse<Response> response4 = 지하철_노선_생성_요청(new LineRequest(invalidCase4, "bg-red-900", 강남역.getId(), downStation.getId(), 10));

        // then
        지하철_노선_생성_실패됨(response1);
        지하철_노선_생성_실패됨(response2);
        지하철_노선_생성_실패됨(response3);
        지하철_노선_생성_실패됨(response4);
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

    @DisplayName("등록되지 않은 지하철 노선을 조회할 수 없다.")
    @Test
    void getLineNotRegistered() {
        // given
        final Line lineWithInvalidLineId = new Line(100000L, "신분당선", "bg-red-600");
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(LineResponse.of(lineWithInvalidLineId));

        // then
        지하철_노선_등록되어있지_않음(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 수정 시 미리 등록된 노선의 이름을 사용할 수 없다.")
    @Test
    void updateLineWithDuplicateName() {
        // given
        final String duplicateLineName = "신분당선";
        LineRequest lineRequest1 = new LineRequest(duplicateLineName, "bg-red-600", 강남역.getId(), downStation.getId(), 10, 900);
        지하철_노선_등록되어_있음(lineRequest1);
        LineRequest lineRequest2 = new LineRequest("구신분당선", "bg-pink-600", 강남역.getId(), downStation.getId(), 15, 1200);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        LineRequest lineRequest3 = new LineRequest(duplicateLineName, "bg-pink-600", 강남역.getId(), downStation.getId(), 15, 1200);
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse2, lineRequest3);

        // then
        지하철_노선_이름_중복되어_수정_실패(response);
    }

    @DisplayName("지하철 노선을 수정 시 미리 등록된 노선의 색상을 사용할 수 없다.")
    @Test
    void updateLineWithDuplicateColor() {
        // given
        final String duplicateColor = "bg-red-600";
        LineRequest savedLine = new LineRequest("신분당선", duplicateColor, 강남역.getId(), downStation.getId(), 10, 900);
        지하철_노선_등록되어_있음(savedLine);

        LineRequest lineRequest2 = new LineRequest("구신분당선", "bg-pink-600", 강남역.getId(), downStation.getId(), 15, 1200);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        LineRequest duplicateColorRequest = new LineRequest("구신분당선", duplicateColor, 강남역.getId(), downStation.getId(), 15, 1200);
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse2, duplicateColorRequest);

        // then
        지하철_노선_색상_중복되어_수정_실패(response);
    }

    @DisplayName("지하철 노선 수정 시 이름은 2자 이상 한글/숫자만 가능하며, 공백은 허용하지 않는다.")
    @Test
    void updateLineWithInvalidName() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);
        String invalidCase1 = "역";
        String invalidCase2 = "line";
        String invalidCase3 = "       ";
        String invalidCase4 = "$#*^";

        final LineRequest invalidLineRequest1 = new LineRequest(invalidCase1, "bg-red-600", 강남역.getId(), downStation.getId(), 10);
        final LineRequest invalidLineRequest2 = new LineRequest(invalidCase2, "bg-red-700", 강남역.getId(), downStation.getId(), 10);
        final LineRequest invalidLineRequest3 = new LineRequest(invalidCase3, "bg-red-800", 강남역.getId(), downStation.getId(), 10);
        final LineRequest invalidLineRequest4 = new LineRequest(invalidCase4, "bg-red-900", 강남역.getId(), downStation.getId(), 10);

        // when
        ExtractableResponse<Response> response1 = 지하철_노선_수정_요청(lineResponse, invalidLineRequest1);
        ExtractableResponse<Response> response2 = 지하철_노선_수정_요청(lineResponse, invalidLineRequest2);
        ExtractableResponse<Response> response3 = 지하철_노선_수정_요청(lineResponse, invalidLineRequest3);
        ExtractableResponse<Response> response4 = 지하철_노선_수정_요청(lineResponse, invalidLineRequest4);

        // then
        지하철_노선_수정_실패됨(response1);
        지하철_노선_수정_실패됨(response2);
        지하철_노선_수정_실패됨(response3);
        지하철_노선_수정_실패됨(response4);
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

    @DisplayName("등록되지 않은 지하철 노선을 삭제할 수 없다.")
    @Test
    void deleteLineNotRegistered() {
        // given
        final Line lineWithInvalidLineId = new Line(100000L, "신분당선", "bg-red-600");
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(LineResponse.of(lineWithInvalidLineId));

        // then
        지하철_노선_등록되어있지_않음(response);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
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

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineRequest params) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse) {
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
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("2자 이상 한글/숫자로 구성된 노선 이름만 허용합니다.");
    }

    public static void 지하철_노선_이름이_중복되어_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("이미 등록된 노선 이름입니다.");
    }

    public static void 지하철_노선_색상이_중복되어_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("이미 등록된 노선 색상입니다.");
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_등록되어있지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("노선이 등록되어 있지 않습니다.");
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
        assertThat(resultResponse.getExtraFare()).isEqualTo(lineResponse.getExtraFare());
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

    public static void 지하철_노선_수정_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("2자 이상 한글/숫자로 구성된 노선 이름만 허용합니다.");
    }

    public static void 지하철_노선_이름_중복되어_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("이미 등록된 노선 이름입니다.");
    }

    public static void 지하철_노선_색상_중복되어_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("이미 등록된 노선 색상입니다.");
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
