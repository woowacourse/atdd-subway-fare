package wooteco.subway.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.MemberResponse;

import java.util.HashMap;
import java.util.Map;

import static wooteco.subway.member.MemberAcceptanceTest.getMyMemberInfo;
import static wooteco.subway.util.TestUtil.*;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private ObjectMapper objectMapper = new ObjectMapper();

    public static TokenResponse 로그인되어_있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        return response.as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> requestLogin(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    @DisplayName("Bearer Auth 정상적으로 로그인한다.")
    @Test
    void myInfoWithBearerAuth() throws JsonProcessingException {
        registerMember(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = login(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = getMyMemberInfo(tokenResponse.getAccessToken());
        String message = objectMapper.writeValueAsString(response.as(MemberResponse.class));

        assertResponseStatus(response, HttpStatus.OK);
        assertResponseMessage(response, message);
    }

    @DisplayName("Bearer Auth 로그인 실패한다.")
    @Test
    void myInfoWithBadBearerAuth() {
        registerMember(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = requestLogin("wrongemail", PASSWORD);

        assertResponseStatus(response, HttpStatus.UNAUTHORIZED);
        assertResponseMessage(response, "잘못된 이메일입니다.");
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰의 경우 조회에 실패한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = new TokenResponse("accesstoken");

        ExtractableResponse<Response> response = getMyMemberInfo(tokenResponse.getAccessToken());

        assertResponseStatus(response, HttpStatus.UNAUTHORIZED);
        assertResponseMessage(response, "유효하지 않은 토큰입니다.");
    }
}
