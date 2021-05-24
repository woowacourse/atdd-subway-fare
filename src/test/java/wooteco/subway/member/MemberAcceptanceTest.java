package wooteco.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.ChangeAgeRequest;
import wooteco.subway.member.dto.ChangePasswordRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_PASSWORD = "new_password";
    public static final int NEW_AGE = 30;

    @DisplayName("회원 가입")
    @Test
    void registerMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
    }

    @DisplayName("회원 정보 조회")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자);
        회원_정보_조회됨(findResponse, EMAIL, AGE);
    }

    @DisplayName("비밀번호 수정 - 현재 비밀번호와 새로운 비밀번호를 넘기면 비밀번호가 수정된다.")
    @Test
    void changePasswordCorrectly() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> changePasswordResponse = 내_회원_정보_비밀번호_수정_요청(사용자, PASSWORD, NEW_PASSWORD);

        // then
        회원_정보_비밀번호_수정됨(changePasswordResponse);
    }

    @DisplayName("비밀번호 수정 - 현재 비밀번호가 틀리면 401을 던진다.")
    @Test
    void changePasswordUnCorrectly() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> changePasswordResponse = 내_회원_정보_비밀번호_수정_요청(사용자, "틀린비밀번호", NEW_PASSWORD);

        // then
        assertThat(changePasswordResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("나이 수정 - 회원 정보에서 나이를 수정할 수 있다.")
    @Test
    void changeAge() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> changeAgeResponse = 내_회원_정보_나이_수정_요청(사용자, NEW_AGE);

        // then
        회원_정보_수정됨(changeAgeResponse);
        assertThat((int) changeAgeResponse.body().jsonPath().get("age")).isEqualTo(NEW_AGE);
    }

    @DisplayName("나이 수정 - 수정하고자 하는 나이가 음수거나 100살을 넘으면 400을 던진다.")
    @Test
    void notValidChangeAge() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> changeAgeResponse1 = 내_회원_정보_나이_수정_요청(사용자, -1);
        ExtractableResponse<Response> changeAgeResponse2 = 내_회원_정보_나이_수정_요청(사용자, 101);

        //
        assertThat(changeAgeResponse1.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(changeAgeResponse2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("회원 정보 삭제")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자);

        // then
        회원_삭제됨(deleteResponse);
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

    private ExtractableResponse<Response> 내_회원_정보_비밀번호_수정_요청(TokenResponse tokenResponse, String password, String newPassword) {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(password, newPassword);

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(changePasswordRequest)
            .when().put("/api/members/me/pw")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 내_회원_정보_나이_수정_요청(TokenResponse tokenResponse, int newAge) {
        ChangeAgeRequest changeAgeRequest = new ChangeAgeRequest(newAge);

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(changeAgeRequest)
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
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_정보_비밀번호_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
