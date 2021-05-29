package wooteco.subway.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.member.MemberAcceptanceTest.회원_정보_조회됨;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(tokenResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL + "OTHER");
        params.put("password", PASSWORD);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = new TokenResponse("accesstoken");

        RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("로그인 되지 않은 사용자는 지하철 노선도 서비스를 조회 (GET) 할 수 있음")
    @Test
    void notLoggedInUserOnlyUseGetMethod() {
        final List<String> services = Arrays.asList("stations", "lines");
        for (String serviceUrl : services) {
            ExtractableResponse<Response> request = RestAssured
                    .given().log().all()
                    .when().get(serviceUrl)
                    .then().log().all()
                    .extract();
            assertThat(request.statusCode()).isEqualTo(200);
        }
    }

    @DisplayName("로그인 되지 않은 사용자는 지하철 노선도 서비스의 자원에 POST 할 수 없음")
    @Test
    void notLoggedInUserCannotUsePostMethod() {
        StationRequest stationRequest = new StationRequest("로그인안됨역");

        ExtractableResponse<Response> stationsRequest = RestAssured
                .given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(stationsRequest.statusCode()).isEqualTo(401);

        LineRequest params = new LineRequest("로그인안됨선", "로그인색", 1L, 2L, 0);

        ExtractableResponse<Response> lineRequest = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();

        assertThat(lineRequest.statusCode()).isEqualTo(401);
    }


    @DisplayName("로그인 되지 않은 사용자는 지하철 노선도 서비스의 자원에 DELETE 할 수 없음")
    @Test
    void notLoggedInUserCannotUseDeleteMethod() {
        final StationResponse 새로운역 = 지하철역_등록되어_있음("새로운역");

        ExtractableResponse<Response> stationsRequest = RestAssured
                .given().log().all()
                .when().delete("/stations/" + 새로운역.getId())
                .then().log().all()
                .extract();

        assertThat(stationsRequest.statusCode()).isEqualTo(401);

        ExtractableResponse<Response> lineRequest = RestAssured
                .given().log().all()
                .when().delete("/lines/1")
                .then().log().all()
                .extract();

        assertThat(lineRequest.statusCode()).isEqualTo(401);
    }


    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, Integer age) {
        return 회원_생성을_요청(email, password, age);
    }

    public static TokenResponse 로그인되어_있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        return response.as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/login/token").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all().
                auth().oauth2(tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract();
    }
}
