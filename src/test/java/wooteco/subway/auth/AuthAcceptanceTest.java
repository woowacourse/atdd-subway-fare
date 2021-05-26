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

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.member.MemberAcceptanceTest.회원_정보_조회됨;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("로그인 - Bearer Auth 로그인 실패")
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
            .when().post("/api/login")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("내 회원 정보 요청 - Bearer Auth")
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

    @DisplayName("내 회원 정보 요청 - Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = new TokenResponse("accesstoken");

        RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/members/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("이메일 중복 확인 - 존재 하지 않는 이메일 중복 확인시 204")
    @Test
    void nonDuplicateEmail() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@naver.com");
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/api/members/exists")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("이메일 중복 확인 - 이미 존재하는 이메일 중복 확인시 400")
    @Test
    void duplicateEmail() {
        // given
        String email = "test@naver.com";
        회원_등록되어_있음(email, PASSWORD, AGE);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/api/members/exists")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이메일 중복 확인 - 요청한 이메일이 이메일 형식이 아닌 경우 400")
    @Test
    void nonEmail() {
        // given
        String email = "testemail";
        ExtractableResponse<Response> nonEmailCreateResponse = 회원_생성을_요청(email, PASSWORD, AGE);

        assertThat(nonEmailCreateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
                post("/api/login").
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
