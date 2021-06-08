package wooteco.subway.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.member.MemberAcceptanceTest.회원_정보_조회됨;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @Test
    @DisplayName("올바른 회원 가입 요청")
    void validSingUp() {
        ExtractableResponse<Response> response = 회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("올바르지 않은 이메일 형식 회원 가입 요청")
    @ParameterizedTest(name = "{index}: email = {arguments}")
    @ValueSource(strings = {"notEmail@", "test", "@email.com", " "})
    void invalidEmailSingUp(String email) {
        ExtractableResponse<Response> response = 회원_등록되어_있음(email, PASSWORD, AGE);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Bearer Auth")
    void myInfoWithBearerAuth() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(tokenResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @Test
    @DisplayName("Bearer Auth 로그인 실패 :: invalid email address")
    void InvalidEmailAddress() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        TokenRequest tokenRequest = new TokenRequest("", PASSWORD);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when()
                .post("/login/token")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Bearer Auth 로그인 실패 :: non exist email address")
    void myInfoWithNonExistEmailAddress() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        TokenRequest tokenRequest = new TokenRequest(EMAIL + "other", PASSWORD);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when()
                .post("/login/token")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Bearer Auth 로그인 실패 :: invalid password")
    void myInfoWithInvalidPassword() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD + "other");

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when()
                .post("/login/token")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Bearer Auth 유효하지 않은 토큰")
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
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when()
                .post("/login/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given()
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
