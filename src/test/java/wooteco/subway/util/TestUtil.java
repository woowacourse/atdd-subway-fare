package wooteco.subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.MemberRequest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtil {

    public static ExtractableResponse<Response> registerMember(String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .extract()
                .as(TokenResponse.class);
    }

    public static void assertResponseStatus(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static void assertResponseMessage(ExtractableResponse<Response> response, String message) {
        assertThat(response.body().asString()).isEqualTo(message);
    }
}
