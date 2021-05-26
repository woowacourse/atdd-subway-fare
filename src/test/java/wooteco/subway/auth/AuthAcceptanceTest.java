package wooteco.subway.auth;

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
import wooteco.subway.exception.ExceptionResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.member.MemberAcceptanceTest.회원_정보_조회됨;

public class AuthAcceptanceTest extends AcceptanceTest {

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
        TokenRequest token = new TokenRequest(EMAIL, PASSWORD + "ASD");

        ExtractableResponse<Response> response = 로그인_요청(token);

        assertThat(response.as(ExceptionResponse.class).getMessage()).isEqualTo("이메일 혹은 비밀번호를 다시 확인해주세요");
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = new TokenResponse("accesstoken");

        ExtractableResponse<Response> response = 토큰_조회_요청(tokenResponse);

        ExceptionResponse exception = response.as(ExceptionResponse.class);
        assertThat(exception.getMessage()).isEqualTo("다시 로그인 후 시도해주세요");
    }

    private ExtractableResponse<Response> 토큰_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 로그인_요청(TokenRequest token) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(token)
                .when().post("/api/login")
                .then().log().all()
                .extract();
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

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(tokenRequest).
                when().post("/api/login").
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
                get("/api/members/me").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract();
    }
}
