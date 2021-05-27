package wooteco.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.EmailCheckRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

@Sql("classpath:tableInit.sql")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String NEW_EMAIL = "new_email@email.com";

    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String INVALID_PASSWORD = "12";
    public static final String INVALID_PASSWORD_OVER = "123456789012345678901234567890";

    public static final int AGE = 20;
    public static final int NEW_AGE = 30;
    public static final int INVALID_AGE = -1;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(사용자, EMAIL, NEW_PASSWORD, NEW_AGE);
        회원_정보_수정됨(updateResponse);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자);
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("이메일이 이미 저장되어있는지 확인한다.")
    @Test
    void confirmEmail() {
        EmailCheckRequest emailCheckRequest = new EmailCheckRequest(EMAIL);
        ExtractableResponse<Response> createResponse = 이메일_중복_확인_요청(emailCheckRequest);
        이메일_중복되지_않음(createResponse);
    }

    @DisplayName("이메일이 이미 저장되어있는지 확인한다.")
    @Test
    void confirmEmailWhenDuplicated() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        EmailCheckRequest emailCheckRequest = new EmailCheckRequest(EMAIL);
        ExtractableResponse<Response> confirmEmailResponse = 이메일_중복_확인_요청(emailCheckRequest);
        이메일_중복_예외(confirmEmailResponse);
    }

    @DisplayName("회원 가입 시 이메일은 중복될 수 없다.")
    @Test
    void duplicatedEmail() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> createResponseWithDuplicatedEmail = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        이메일_중복_예외(createResponseWithDuplicatedEmail);
    }

    @DisplayName("회원 가입 시 비밀번호는 4자 이상, 20자 이하이여야한다.")
    @Test
    void validatePassword() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, INVALID_PASSWORD, AGE);
        회원_생성되지_않음(createResponse);
    }

    @DisplayName("회원 가입 시 비밀번호는 4자 이상, 20자 이하이여야한다.")
    @Test
    void validatePassword2() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, INVALID_PASSWORD_OVER, AGE);
        회원_생성되지_않음(createResponse);
    }

    @DisplayName("회원 가입 시 나이는 양수여야한다.")
    @Test
    void validateAge() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, INVALID_AGE);
        회원_생성되지_않음(createResponse);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 이메일_중복_확인_요청 (EmailCheckRequest emailCheckRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(emailCheckRequest)
                .when().post("/members/email-check")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse, String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_삭제_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_생성되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 이메일_중복되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 이메일_중복_예외(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}
