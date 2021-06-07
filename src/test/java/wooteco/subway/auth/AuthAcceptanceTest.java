package wooteco.subway.auth;

import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.member.MemberAcceptanceTest.회원_정보_조회됨;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;

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

    @DisplayName("Bearer Auth 로그인 실패 - 존재하지 않는 email")
    @Test
    void myInfoWithBadBearerAuth_nonExistentEmail() {
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

    @DisplayName("Bearer Auth 로그인 실패 - email 형식이 아님")
    @Test
    void myInfoWithBadBearerAuth_nonEmailFormat() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "notEmailFormat");
        params.put("password", PASSWORD);

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Bearer Auth 로그인 실패 - 비어있거나 null인 email")
    @NullAndEmptySource
    @ParameterizedTest
    void myInfoWithBadBearerAuth_emptyEmail(String email) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", PASSWORD);

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Bearer Auth 로그인 실패 - 비밀번호 불일치")
    @Test
    void myInfoWithBadBearerAuth_wrongPassword() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", "wrongPassword");

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer Auth 로그인 실패 - 비밀번호가 null이거나 비어있음")
    @NullAndEmptySource
    @ParameterizedTest
    void myInfoWithBadBearerAuth_emptyPassword(String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", EMAIL);
        params.put("password", password);

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
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
