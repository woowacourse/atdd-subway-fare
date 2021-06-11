package wooteco.subway.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.member.MemberAcceptanceTest.회원_정보_조회됨;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.MismatchIdPasswordException;
import wooteco.subway.member.dto.MemberRequest;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("Bearer Auth 로그인 성공")
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

    @DisplayName("Bearer Auth 이메일은 일치하지만 비밀번호가 틀린 경우 로그인 실패")
    @Test
    void wrongPasswordBearerAuth() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD + "OTHER");

        // when
        String detailErrorMessage = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/api/login/token")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .extract().jsonPath().get("error");

        // then
        assertThat(detailErrorMessage).isEqualTo(new MismatchIdPasswordException().error());
    }

    @DisplayName("Bearer Auth 존재하지 않은 이메일로 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenRequest tokenRequest = new TokenRequest(EMAIL + "OTHER", PASSWORD);

        // when
        String detailErrorMessage = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/api/login/token")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .extract().jsonPath().get("error");

        // then
        assertThat(detailErrorMessage).isEqualTo(new MismatchIdPasswordException().error());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        TokenResponse tokenResponse = new TokenResponse("accesstoken");

        // when
        String detailErrorMessage = RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/members/me")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .extract().jsonPath().get("error");

        // then
        assertThat(detailErrorMessage).isEqualTo(new AuthorizationException().error());
    }

    @DisplayName("Bearer Auth 토큰 없이 API 요청")
    @Test
    void myInfoWithNonBearerAuth() {
        // given

        // when
        String detailErrorMessage = RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/members/me")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .extract().jsonPath().get("error");

        // then
        assertThat(detailErrorMessage).isEqualTo(new AuthorizationException().error());
    }

    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/api/members")
            .then().log().all()
            .extract();
    }

    public static TokenResponse 로그인되어_있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        return response.as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest).
                when().post("/api/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }
}
