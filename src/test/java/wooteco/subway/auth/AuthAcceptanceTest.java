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

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        createMember(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = login(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = getMemberByToken(tokenResponse);

        // then
        assertMember(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        createMember(EMAIL, PASSWORD, AGE);

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

    private ExtractableResponse<Response> getMemberByToken(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE)
                .when().
                get("/members/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
