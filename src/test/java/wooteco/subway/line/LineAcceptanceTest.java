package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineDetailResponse;
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
    private LineRequest 신분당선;
    private LineRequest 구신분당선;
    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);
        // given
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        StationResponse 광교역 = 지하철역_등록되어_있음("광교역", tokenResponse);

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        구신분당선 = new LineRequest("구신분당선", "bg-black-500", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선, tokenResponse);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성하면, 400 에러를 받는다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(신분당선, tokenResponse);
        LineRequest newRequest = new LineRequest(신분당선.getName(), 구신분당선.getColor(), 구신분당선.getUpStationId(), 구신분당선.getDownStationId(), 구신분당선.getDistance());

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(newRequest, tokenResponse);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 색깔로 지하철 노선을 생성하면, 400 에러를 받는다.")
    @Test
    void createLineWithDuplicateColor() {
        // given
        지하철_노선_등록되어_있음(신분당선, tokenResponse);
        LineRequest newRequest = new LineRequest(구신분당선.getName(), 신분당선.getColor(), 구신분당선.getUpStationId(), 구신분당선.getDownStationId(), 구신분당선.getDistance());

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(newRequest, tokenResponse);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철노선 이름에 공백이 있거나 길이가 2미만, 20 초과인경우, 400 에러를 받는다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"에", "우아한테크코스검프에어바다포츈우기화이팅짱"})
    void createStationFail(String name) {
        //given
        LineRequest lineRequest = new LineRequest(name, 신분당선.getColor(), 신분당선.getUpStationId(), 신분당선.getDownStationId(), 신분당선.getDistance());

        //when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest, tokenResponse);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(신분당선, tokenResponse);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(구신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("지하철 노선(상세) 목록을 조회한다.")
    @Test
    void getDetailLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(신분당선, tokenResponse);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(구신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_상세_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_상세_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("지하철 상세노선을 조회한다.")
    @Test
    void getLineDetail() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_상세_조회_요청(lineResponse);

        // then
        지하철_노선_상세_응답됨(response, lineResponse);
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

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare, TokenResponse tokenResponse) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
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

    private static ExtractableResponse<Response> 지하철_노선_상세_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/detail")
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

    public static ExtractableResponse<Response> 지하철_노선_상세_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/detail/{lineId}", response.getId())
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

    public static void 지하철_노선_상세_응답됨(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineDetailResponse resultResponse = response.as(LineDetailResponse.class);
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

    public static void 지하철_노선_상세_목록_포함됨(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineDetailResponse.class).stream()
                .map(LineDetailResponse::getId)
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
