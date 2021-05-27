package wooteco.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
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
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@Sql("classpath:tableInit.sql")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";
    private static final String abc = "abc";
    private static final String _ = "1";
    private static final String 공백이_포함된_역 = "공백 공백";
    private static final StationResponse 예시_지하철역_응답 = new StationResponse(100L, 역삼역);

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_401_실패됨(response);
    }

    @DisplayName("로그인 사용자가 지하철역을 생성한다.")
    @Test
    void createStationWithLoginMember() {
        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철역_생성_요청(강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        로그인_사용자_지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철역_생성_요청(강남역);

        // then
        지하철역_생성_실패됨_중복(response);
    }

    @DisplayName("영어 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithInvalidPattern() {
        // given-when
        ExtractableResponse<Response> response = 로그인_사용자_지하철역_생성_요청(abc);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("한글자로 지하철역을 생성한다.")
    @Test
    void createStationWithInvalidPattern2() {
        // given-when
        ExtractableResponse<Response> response = 로그인_사용자_지하철역_생성_요청(_);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("공백이 포함된 역명으로 지하철역을 생성한다.")
    @Test
    void createStationWithEmptySpace() {
        // given-when
        ExtractableResponse<Response> response = 로그인_사용자_지하철역_생성_요청(공백이_포함된_역);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        StationResponse stationResponse1 = 로그인_사용자_지하철역_등록되어_있음(강남역);
        StationResponse stationResponse2 = 로그인_사용자_지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = 로그인_사용자_지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse);

        // then
        지하철역_401_실패됨(response);
    }

    @DisplayName("로그인한 사용자가 지하철역을 제거한다.")
    @Test
    void deleteStationWithLoginMember() {
        // given
        StationResponse stationResponse = 로그인_사용자_지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철역_제거_요청(stationResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("존재하지 않는 지하철역을 제거한다.")
    @Test
    void deleteWithNotFoundStation() {
        //given
        StationResponse stationResponse = 로그인_사용자_지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 로그인_사용자_지하철역_제거_요청(예시_지하철역_응답);

        // then
        지하철역_삭제안됨_존재하지_않는_역(response);
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static StationResponse 로그인_사용자_지하철역_등록되어_있음(String name) {
        return 로그인_사용자_지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_사용자_지하철역_생성_요청(String name) {
        AuthAcceptanceTest.회원_등록되어_있음("email@email.com","1234",15);
        final TokenResponse tokenResponse
                = AuthAcceptanceTest.로그인되어_있음("email@email.com", "1234");

        return RestAssured
                .given().log().all()
                .body(new StationRequest(name))
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse) {
        return RestAssured
                .given().log().all()
                .when().delete("/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_사용자_지하철역_제거_요청(StationResponse stationResponse) {
        AuthAcceptanceTest.회원_등록되어_있음("email@email.com","1234",15);
        final TokenResponse tokenResponse
                = AuthAcceptanceTest.로그인되어_있음("email@email.com", "1234");

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨_중복(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_401_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_삭제안됨_존재하지_않는_역(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
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
}
