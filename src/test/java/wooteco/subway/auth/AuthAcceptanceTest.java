package wooteco.subway.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    public static ExtractableResponse<Response> loginRequest(TokenRequest tokenRequest) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when()
                .post("/api/login/token")
                .then().extract();
    }

    @Test
    @DisplayName("ID가 Email 형식이 아닐 경우의 로그인 요청")
    public void loginWhenInvalidEmail() {
        TokenRequest tokenRequest = new TokenRequest("invalid", "password");
        ExtractableResponse<Response> response = loginRequest(tokenRequest);
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_INPUT");
    }

    @Test
    @DisplayName("비밀번호가 blank일 경우의 로그인 요청")
    public void loginWhenInvalidPassword() {
        TokenRequest tokenRequest = new TokenRequest("kang@kang.com", " ");
        ExtractableResponse<Response> response = loginRequest(tokenRequest);
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_INPUT");
    }

    @Test
    @DisplayName("비밀번호가 틀린 경우의 로그인 요청")
    public void loginWhenMisMatchCredential() {
        TokenRequest tokenRequest = new TokenRequest("email@email.com", "wrongPwd");
        ExtractableResponse<Response> response = loginRequest(tokenRequest);
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("MISMATCH_ID_PASSWORD");
    }

    @Test
    @DisplayName("정상적인 로그인 요청")
    public void loginWhenValidRequest() {
        TokenRequest tokenRequest = new TokenRequest("email@email.com", "password");
        ExtractableResponse<Response> response = loginRequest(tokenRequest);
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
