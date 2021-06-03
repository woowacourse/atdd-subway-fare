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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.member.MemberAcceptanceTest.회원_정보_조회됨;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("올바른 회원 가입 요청")
    @Test
    void validSingUp() {
        ExtractableResponse<Response> response = 회원_등록되어_있음("email@email.com", PASSWORD, AGE);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("올바르지 않은 회원 가입 요청")
    @Test
    void invalidSingUp() {
        ExtractableResponse<Response> response = 회원_등록되어_있음("notEmail@", PASSWORD, AGE);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

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

    @DisplayName("Bearer Auth 로그인 실패 :: invalid email address")
    @Test
    void InvalidEmailAddress() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        Map<String, String> params = new HashMap<>();
        params.put("email", "");
        params.put("password", PASSWORD);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/login/token")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Bearer Auth 로그인 실패 :: non exist email address")
    @Test
    void myInfoWithNonExistEmailAddress() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL + "other");
        params.put("password", PASSWORD);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/login/token")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 로그인 실패 :: invalid password")
    @Test
    void myInfoWithInvalidPassword() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", PASSWORD + "other");

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/login/token")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = new TokenResponse("accesstoken");

        RestAssured
                .given()
                .auth()
                .oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
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

        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/login/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured.given()
                .auth()
                .oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
