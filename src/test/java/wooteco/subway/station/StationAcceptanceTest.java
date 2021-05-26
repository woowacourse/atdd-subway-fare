package wooteco.subway.station;

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
import wooteco.subway.member.MemberAcceptanceTest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_생성_요청;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";
    private static TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenResponse = MemberAcceptanceTest.회원_로그인된_상태();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("유효하지 않은 토큰으로 지하철역을 생성한다.")
    @Test
    void createStationByStrangeToken() {
        // when
        ExtractableResponse<Response> response = 잘못된_토큰으로_지하철역_생성_요청(강남역);

        // then
        잘못된_토큰으로_요청을_보냄(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_이름중복으로_실패됨(response);
    }

    @DisplayName("지하철역 이름 null 값으로 생성을 요청한다.")
    @Test
    void createStationWithNullName() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(null);

        // then
        잘못된_입력값으로_요청을_보냄(response);
    }

    @DisplayName("지하철역 이름이 비어있는 값으로 생성을 요청한다.")
    @Test
    void createStationWithEmptyName() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청("");

        // then
        잘못된_입력값으로_요청을_보냄(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void updateStation() {
        //given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        //when
        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse.getId(), 역삼역);

        //then
        지하철역_수정됨(response);
    }

    @DisplayName("잘못된 토큰으로 지하철역을 수정한다.")
    @Test
    void updateStationWithStrangeToken() {
        //given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        //when
        ExtractableResponse<Response> response = 잘못된_토큰으로_지하철역_수정_요청(stationResponse.getId(), 강남역);

        //then
        잘못된_토큰으로_요청을_보냄(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 수정한다.")
    @Test
    void updateStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);
        StationResponse stationResponse = 지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse.getId(), 강남역);

        // then
        지하철역_이름중복으로_실패됨(response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("잘못된 토큰으로 지하철역을 제거한다.")
    @Test
    void deleteStationWithStrangeToken() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 유효하지_않은_토큰으로_지하철역_제거_요청(stationResponse);

        // then
        잘못된_토큰으로_요청을_보냄(response);
    }

    @DisplayName("노선의 구간에 등록되어 있는 지하철역을 제거한다.")
    @Test
    void deleteStationAlreadyInsertSection() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역);
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", stationResponse1.getId(), stationResponse2.getId(), 10);

        지하철_노선_생성_요청(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse1);

        // then
        노선의_구간에_등록_되어있는_지하철역을_제거함(response);
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static StationResponse 지하철역_등록되어_있음(String name, TokenResponse tokenResponse) {
        return 지하철역_생성_요청(name, tokenResponse).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
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

    public static ExtractableResponse<Response> 잘못된_토큰으로_지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "이상한 토큰")
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, TokenResponse tokenResponse) {
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

    public static ExtractableResponse<Response> 지하철역_수정_요청(Long id, String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/stations/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_토큰으로_지하철역_수정_요청(Long id, String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "이상한 토큰")
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/stations/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/api/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 유효하지_않은_토큰으로_지하철역_제거_요청(StationResponse stationResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "이상한 토큰")
                .when().delete("/api/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_이름중복으로_실패됨(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("이미 존재하는 지하철 역입니다");
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_수정됨(ExtractableResponse<Response> response) {
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

    private static void 잘못된_토큰으로_요청을_보냄(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("다시 로그인 후 시도해주세요");
    }

    private static void 잘못된_입력값으로_요청을_보냄(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("입력되지 않은 항목을 확인해주세요");
    }

    private void 노선의_구간에_등록_되어있는_지하철역을_제거함(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("이미 노선에 등록된 지하철 역입니다");
    }
}
