package wooteco.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.*;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

public class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        String Email = "new@new.com";
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(Email, PASSWORD, AGE);
        신규_회원_생성됨(createResponse);

        TokenResponse 신규회원 = 로그인되어_있음(Email, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(신규회원);
        신규회원_정보_조회됨(findResponse, Email, AGE);

        ExtractableResponse<Response> updatePwResponse = 내_비밀번호_수정_요청(신규회원, PASSWORD, NEW_PASSWORD);
        회원_정보_수정됨(updatePwResponse);

        ExtractableResponse<Response> updateAgeResponse = 내_나이정보_수정_요청(신규회원, NEW_AGE);
        나이_정보_수정됨(updateAgeResponse);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(신규회원);
        회원_삭제됨(deleteResponse);
    }

    @Test
    @DisplayName("중복된 이메일이 존재하면 예외를 던진다.")
    void testCheckExistsEmail() {
        String Email = "new@new.com";
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(Email, PASSWORD, AGE);
        신규_회원_생성됨(createResponse);

        ExtractableResponse<Response> findResponse = 이메일_중복_확인(Email);
        중복된_이메일_존재함(findResponse);
    }

    @Test
    @DisplayName("중복된 이메일이 존재하지 않는 경우 응답코드 NO_CONTENT.")
    void testCheckExistsEmailWhenNotExists() {
        String Email = "new@new.com";

        ExtractableResponse<Response> findResponse = 이메일_중복_확인(Email);

        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 중복된_이메일_존재함(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private ExtractableResponse<Response> 이메일_중복_확인(String email) {
        EmailCheckRequest request = new EmailCheckRequest(email);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/members/exists")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/api/members")
                .then().log().all()
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

    public static ExtractableResponse<Response> 내_비밀번호_수정_요청(TokenResponse tokenResponse, String password, String newPassword) {
        PasswordRequest passwordRequest = new PasswordRequest(password, newPassword);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(passwordRequest)
                .when().put("/api/members/me/pw")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내_나이정보_수정_요청(TokenResponse tokenResponse, int newAge) {
        AgeRequest ageRequest = new AgeRequest(newAge);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ageRequest)
                .when().put("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_삭제_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/members/1");
    }

    public static void 신규_회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/members/2");
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isEqualTo(1);
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    private void 신규회원_정보_조회됨(ExtractableResponse<Response> findResponse, String email, int age) {
        MemberResponse memberResponse = findResponse.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isEqualTo(2);
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 나이_정보_수정됨(ExtractableResponse<Response> updateAgeResponse) {
        assertThat(updateAgeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateAgeResponse.as(AgeResponse.class).getId()).isEqualTo(2);
        assertThat(updateAgeResponse.as(AgeResponse.class).getAge()).isEqualTo(NEW_AGE);
    }
}
