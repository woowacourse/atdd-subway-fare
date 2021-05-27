package wooteco.subway.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest2 extends AcceptanceTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setRequestAndMemberInfo() {
        jdbcTemplate.update("insert into MEMBER (email, password, age) values ('email@email.com', 'password', 20)");
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

    public static ExtractableResponse<Response> loginRequest(TokenRequest tokenRequest) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when()
                .post("/api/login/token")
                .then().extract();
    }
}
