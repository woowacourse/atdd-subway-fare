package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.AuthAcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineWithSectionsResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.station.StationAcceptanceTest.로그인_사용자_지하철역_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
@Sql("classpath:tableInit.sql")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse downStation;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private LineRequest lineRequest3;
    private LineRequest lineRequest4;
    private LineRequest lineRequest5;
    private LineRequest lineRequest6;
    private LineResponse lineResponse;

    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        AuthAcceptanceTest.회원_등록되어_있음("email@email.com","1234",15);
        tokenResponse = AuthAcceptanceTest.로그인되어_있음("email@email.com", "1234");

        // given
        강남역 = 로그인_사용자_지하철역_등록되어_있음(tokenResponse, "강남역");
        downStation = 로그인_사용자_지하철역_등록되어_있음(tokenResponse, "광교역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-green-600", 강남역.getId(), downStation.getId(), 15);
        lineRequest3 = new LineRequest("abc1", "bg-blue-600", 강남역.getId(), downStation.getId(), 15);
        lineRequest4 = new LineRequest("", "bg-white-600", 강남역.getId(), downStation.getId(), 15);
        lineRequest5 = new LineRequest("신분당선2", "bg-red-600", 강남역.getId(), downStation.getId(), 10);
        lineRequest6 = new LineRequest("신분당 당당 당당", "bg-white-600", 강남역.getId(), downStation.getId(), 15);

        lineResponse = new LineResponse(1L, "신분당선", "bg-red-600", new ArrayList<>());
    }

    @DisplayName("로그인하지 않은 사용자가 지하철 노선을 생성한다.")
    @Test
    void createLineWithNotLoginMember() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_401_응답됨(response);
    }

    @DisplayName("로그인한 사용자가 지하철 노선을 생성한다.")
    @Test
    void createLineWithLoginMember() {
        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_생성_요청(tokenResponse, lineRequest1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("영어가 섞인 이름으로 노선을 생성한다.")
    @Test
    void createLine2() {
        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_생성_요청(tokenResponse, lineRequest3);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("빈칸으로 노선을 생성한다.")
    @Test
    void createLine3() {
        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_생성_요청(tokenResponse, lineRequest4);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("공백이 섞인 이름으로 노선을 생성한다.")
    @Test
    void createLine4() {
        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_생성_요청(tokenResponse, lineRequest6);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest1);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_생성_요청(tokenResponse, lineRequest1);

        // then
        지하철_노선_생성_실패됨_중복(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 색으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateColor() {
        // given
        로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest1);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_생성_요청(tokenResponse, lineRequest5);

        // then
        지하철_노선_생성_실패됨_중복(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest1);
        LineResponse lineResponse2 = 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest2);

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
        LineResponse lineResponse = 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회한다.")
    @Test
    void getNonExistenceLine() {
        // given - when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_404_응답됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정한다.")
    @Test
    void updateNonExistenceLine() {
        // given - when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_수정_요청(lineResponse, lineRequest1);

        // then
        지하철_노선_404_응답됨(response);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest1);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_수정_요청(lineResponse, lineRequest2);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("로그인하지 않은 사용자가 지하철 노선을 수정한다.")
    @Test
    void updateLineWithNotLogin() {
        // given
        LineResponse lineResponse = 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2);

        // then
        지하철_노선_401_응답됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 삭제한다.")
    @Test
    void deleteNonExistenceLine() {
        // given - when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_삭제_요청(tokenResponse, lineResponse);

        // then
        지하철_노선_404_응답됨(response);
    }

    @DisplayName("로그인하지 않은 사용자가 지하철 노선을 제거한다.")
    @Test
    void deleteLineWithNotLogin() {
        // given
        LineResponse lineResponse = 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(lineResponse);

        // then
        지하철_노선_401_응답됨(response);
    }

    @DisplayName("로그인한 사용자가 지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest1);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철_노선_삭제_요청(tokenResponse, lineResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    public static LineWithSectionsResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 로그인_사용자_지하철_노선_등록되어_있음(TokenResponse tokenResponse, String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest);
    }

    public static LineWithSectionsResponse 지하철_추가요금_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int fare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, fare);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 로그인_사용자_지하철_추가요금_노선_등록되어_있음(TokenResponse tokenResponse, String name, String color, StationResponse upStation, StationResponse downStation, int distance, int fare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, fare);
        return 로그인_사용자_지하철_노선_등록되어_있음(tokenResponse, lineRequest);
    }

    public static LineWithSectionsResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineWithSectionsResponse.class);
    }

    public static LineResponse 로그인_사용자_지하철_노선_등록되어_있음(TokenResponse tokenResponse, LineRequest lineRequest) {
        return 로그인_사용자_지하철_노선_생성_요청(tokenResponse, lineRequest).as(LineResponse.class);
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

    public static ExtractableResponse<Response> 로그인_사용자_지하철_노선_생성_요청(TokenResponse tokenResponse, LineRequest params) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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

    public static ExtractableResponse<Response> 로그인_사용자_지하철_노선_수정_요청(LineResponse response, LineRequest params) {
        AuthAcceptanceTest.회원_등록되어_있음("email@email.com","1234",15);
        final TokenResponse tokenResponse
                = AuthAcceptanceTest.로그인되어_있음("email@email.com", "1234");

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(LineResponse lineResponse) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_사용자_지하철_노선_삭제_요청(TokenResponse tokenResponse, LineResponse response) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + response.getId())
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

    private void 지하철_노선_404_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static void 지하철_노선_생성_실패됨_중복(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 지하철_노선_401_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineWithSectionsResponse resultResponse = response.as(LineWithSectionsResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineWithSectionsResponse.class).stream()
                .map(LineWithSectionsResponse::getId)
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
