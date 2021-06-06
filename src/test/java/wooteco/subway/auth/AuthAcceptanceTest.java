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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthExceptionSet;

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

    @DisplayName("로그인이 필요한 기능에서 토큰이 없으면 에러가 발생한다.")
    @Test
    void showMemberInfoWithNotExistToken() {
        ExtractableResponse<Response> deleteResponse = 토큰없이_조회_요청();
        에러가_발생한다(deleteResponse, AuthExceptionSet.NOT_EXIST_TOKEN_EXCEPTION);
    }

    @DisplayName("이메일이 일치하지 않을 경우 에러가 발생한다.")
    @Test
    void loginWithNotExistEmail() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = 로그인_요청("abc@naver.com", PASSWORD);
        에러가_발생한다(response, AuthExceptionSet.NOT_EXIST_EMAIL_EXCEPTION);
    }

    @DisplayName("이메일이 일치하지 않을 경우 에러가 발생한다.")
    @Test
    void loginWithIllegalPassword() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = 로그인_요청(EMAIL, "1234");
        에러가_발생한다(response, AuthExceptionSet.ILLEGAL_PASSWORD_EXCEPTION);
    }

    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password,
        Integer age) {
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

    public static TokenResponse 회원가입_토큰가져오기() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        return 로그인되어_있음(EMAIL, PASSWORD);
    }

    public ExtractableResponse<Response> 토큰없이_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .extract();
    }
}
