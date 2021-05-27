package wooteco.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest2.loginRequest;

public class MemberAcceptanceTest2 extends AcceptanceTest {

    @Test
    @DisplayName("Email 유효하지 않을 경우의 회원가입")
    public void registerMemberWhenInvalidEmail() {
        MemberRequest memberRequest = new MemberRequest("asdf", "password", 20);
        ExtractableResponse<Response> response = createMemberRequest(memberRequest);
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_INPUT");
    }

    @Test
    @DisplayName("Password 유효하지 않을 경우의 회원가입")
    public void registerMemberWhenInvalidPassword() {
        MemberRequest memberRequest = new MemberRequest("asdf@asdf.com", " ", 20);
        ExtractableResponse<Response> response = createMemberRequest(memberRequest);
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_INPUT");
    }

    @Test
    @DisplayName("Age 유효하지 않을 경우의 회원가입")
    public void registerMemberWhenInvalidAge() {
        MemberRequest memberRequest = new MemberRequest("asdf", "password", null);
        ExtractableResponse<Response> response = createMemberRequest(memberRequest);
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_INPUT");
    }

    @Test
    @DisplayName("중복 이메일 경우의 회원가입")
    public void registerMemberWhenDuplicatedEmail() {
        MemberRequest memberRequest = new MemberRequest("email@email.com", "password", 20);
        ExtractableResponse<Response> response = createMemberRequest(memberRequest);
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("DUPLICATED_ID");
    }

    @Test
    @DisplayName("유효하지 않은 토큰일 때의 내 정보 조회")
    public void findMyInfoWhenNotExistsToken() {
        ExtractableResponse<Response> response = findMemberOfMineRequest("invalidToken");
        assertThat(response.statusCode()).isEqualTo(401);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_TOKEN");
    }

    @Test
    @DisplayName("정상적인 토큰일 때의 내 정보 조회")
    public void findMyInfoWhenValidToken() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = findMemberOfMineRequest(accessToken);
        MemberResponse memberResponse = response.body().as(MemberResponse.class);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(memberResponse).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new MemberResponse(null, "email@email.com", 20));
    }

    @Test
    @DisplayName("유효하지 않은 토큰일 경우의 정보 수정")
    public void updateMyInfoWhenInvalidToken() {
        MemberRequest memberRequest = new MemberRequest("emailmail.com", "password", 30);
        ExtractableResponse<Response> response = updateMemberOfMineRequest(memberRequest, "asdf");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_TOKEN");
    }

    @Test
    @DisplayName("Email 유효하지 않을 경우의 정보 수정")
    public void updateMyInfoWhenInvalidEmail() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        MemberRequest memberRequest = new MemberRequest("emailmail.com", "password", 30);
        ExtractableResponse<Response> response = updateMemberOfMineRequest(memberRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_INPUT");
    }

    @Test
    @DisplayName("Password 유효하지 않을 경우의 정보 수정")
    public void updateMyInfoWhenInvalidPassword() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        MemberRequest memberRequest = new MemberRequest("email@email.com", " ", 30);
        ExtractableResponse<Response> response = updateMemberOfMineRequest(memberRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_INPUT");
    }

    @Test
    @DisplayName("Age 유효하지 않을 경우의 정보 수정")
    public void updateMyInfoWhenInvalidAge() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        MemberRequest memberRequest = new MemberRequest("email@email.com", "password", -5);
        ExtractableResponse<Response> response = updateMemberOfMineRequest(memberRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_INPUT");
    }


    @Test
    @DisplayName("정상적인 내 정보 수정")
    public void updateMyInfo() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        MemberRequest memberRequest = new MemberRequest("email@email.com", "password", 30);
        ExtractableResponse<Response> response = updateMemberOfMineRequest(memberRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(200);
        int age = jdbcTemplate.queryForObject("select age from MEMBER where email = 'email@email.com'", Integer.class);
        assertThat(age).isEqualTo(30);
    }

    @Test
    @DisplayName("유효하지 않은 토큰일 때의 내 정보 삭제")
    public void deleteMyInfoWhenInvalidToken() {
        ExtractableResponse<Response> response = deleteMemberOfMineRequest("asdf");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_TOKEN");
    }

    @Test
    @DisplayName("유효한은 토큰일 때의 내 정보 삭제")
    public void deleteMyInfoWhenValidToken() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = deleteMemberOfMineRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(204);
    }

    @Test
    @DisplayName("이메일 중복체크 - 중복")
    public void checkEmailDuplicatedResultDuplicated() {
        EmailRequest emailRequest = new EmailRequest("email@email.com");
        ExtractableResponse<Response> response = validateEmailRequest(emailRequest);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("DUPLICATED_ID");
    }

    @Test
    @DisplayName("이메일 중복체크 - 허용")
    public void checkEmailDuplicatedResultApprove() {
        EmailRequest emailRequest = new EmailRequest("kang@email.com");
        ExtractableResponse<Response> response = validateEmailRequest(emailRequest);

        assertThat(response.statusCode()).isEqualTo(204);
    }


    public ExtractableResponse<Response> createMemberRequest(MemberRequest memberRequest) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when()
                .post("/api/members")
                .then().extract();
    }

    public ExtractableResponse<Response> findMemberOfMineRequest(String token) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .when()
                .get("/api/members/me")
                .then().extract();
    }

    public ExtractableResponse<Response> updateMemberOfMineRequest(MemberRequest memberRequest, String token) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(memberRequest)
                .when()
                .put("/api/members/me")
                .then().extract();
    }

    public ExtractableResponse<Response> deleteMemberOfMineRequest(String token) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .when()
                .delete("/api/members/me")
                .then().extract();
    }

    public ExtractableResponse<Response> validateEmailRequest(EmailRequest emailRequest) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(emailRequest)
                .when()
                .get("/api/members/check-validation")
                .then().extract();
    }
}
