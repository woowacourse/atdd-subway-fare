package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        회원_생성을_요청(EMAIL, PASSWORD, 10);
        ExtractableResponse<Response> response = 토큰과_함께_지하철역_생성_요청(강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        회원_생성을_요청(EMAIL, PASSWORD, 10);
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철역_생성_요청(강남역);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, 10);
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, 10);
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 토큰과_함께_지하철역_제거_요청(stationResponse);

        // then
        지하철역_삭제됨(response);
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 토큰과_함께_지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 토큰과_함께_지하철역_생성_요청(String name) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
            .given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().post("/api/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 토큰과_함께_지하철역_목록_조회_요청() {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/api/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 토큰과_함께_지하철역_제거_요청(StationResponse stationResponse) {
        String accessToken = 로그인_후_토큰_발급(new TokenRequest(EMAIL, PASSWORD)).jsonPath().get(ACCESS_TOKEN);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().delete("/api/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
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

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/api/members")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_후_토큰_발급(TokenRequest tokenRequest) {
        return RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when()
            .post("/api/login/token")
            .then().extract();
    }
}
