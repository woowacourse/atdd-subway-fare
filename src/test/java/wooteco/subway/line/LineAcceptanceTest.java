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
import wooteco.subway.line.dto.LineUpdateResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
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

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineRequest params) {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

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
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/api/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse response, LineRequest lineRequest) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        LineResponse createLine = response.jsonPath().getObject(".", LineResponse.class);

        assertThat(createLine)
                .usingRecursiveComparison()
                .ignoringFields("id", "stations", "sections")
                .isEqualTo(lineRequest);
    }

    public static void 지하철_노선_생성_실패됨_400(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_생성_실패됨_404(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
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
                .collect(toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response, LineRequest params) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        LineUpdateResponse updatedLine = response.jsonPath().getObject(".", LineUpdateResponse.class);

        assertThat(updatedLine)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(params);
    }

    public static void 지하철_노선_수정_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        광교역 = 지하철역_등록되어_있음("광교역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-old-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성됨(response, lineRequest1);
    }

    @DisplayName("노선 생성 - 기존에 존재하지 않는 역으로 노선을 생성하는 경우 404 에러가 발생한다.")
    @Test
    void createLineWithNotFoundStation() {
        // given
        StationResponse 탄현역 = new StationResponse(3L, "탄현역");

        LineRequest 경의중앙선 =
                new LineRequest("경의중앙선", "bg-blue-600", 강남역.getId(), 탄현역.getId(), 13);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(경의중앙선);

        // then
        지하철_노선_생성_실패됨_404(response);
    }

    @DisplayName("노선 생성 - 기존에 존재하는 지하철 노선 이름으로 생성하는 경우 400 에러가 발생한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성_실패됨_400(response);
    }

    @DisplayName("노선 생성 - 구간 거리가 1 이상이 아닌 경우 400 에러가 발생한다.")
    @Test
    void createLineWithNonPositiveDistance() {
        // given
        LineRequest 신분당선 =
                new LineRequest("경의중앙선", "bg-blue-600", 강남역.getId(), 광교역.getId(), -2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성_실패됨_400(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse));
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

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2);

        // then
        지하철_노선_수정됨(response, lineRequest2);
    }

    @DisplayName("노선 수정 - 기존에 존재하는 노선 이름으로 수정하는 경우 400 에러가 발생한다.")
    @Test
    void updateLineWithDuplicateName() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);
        LineRequest 신분당선_불가능 =
                new LineRequest("신분당선", "bg-blue-600", 강남역.getId(), 광교역.getId(), 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, 신분당선_불가능);

        // then
        지하철_노선_수정_실패됨(response);
    }

    @DisplayName("노선 수정 - 기존에 존재하는 노선 색깔로 수정하는 경우 400 에러가 발생한다.")
    @Test
    void updateLineWithDuplicateColor() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);
        LineRequest 신분당선_불가능 =
                new LineRequest("분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, 신분당선_불가능);

        // then
        지하철_노선_수정_실패됨(response);
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
}
