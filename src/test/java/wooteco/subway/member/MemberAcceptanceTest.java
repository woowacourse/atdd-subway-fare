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
import wooteco.subway.exception.dto.ExceptionResponse;
import wooteco.subway.member.dto.*;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String ERROR_EMAIL = "error.com";
    public static final String NEW_EMAIL = "new_email@email.com";
    public static final String NEW_PASSWORD = "new_password";
    public static final int NEW_AGE = 30;
    public static final String EMPTY_EMAIL = "";
    public static final String EMPTY_PASSWORD = "";
    public static final int ERROR_AGE = 151;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        유효한_이메일로_중복체크(EMAIL);
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> createDuplicateMemberResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        중복_회원을_생성(createDuplicateMemberResponse);

        잘못된_입력값으로_회원가입_요청();

        중복된_이메일로_중복체크(EMAIL);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> findMemberStrangeToken = 잘못된_토큰으로_내_회원_정보_조회_요청(사용자);
        잘못된_토큰으로_요청을_보냄(findMemberStrangeToken);

        ExtractableResponse<Response> updateInfoByErrorAgeResponse = 내_회원_정보_수정_요청(사용자, ERROR_AGE);
        비정상적인_나이_입력값으로_사용(updateInfoByErrorAgeResponse);

        ExtractableResponse<Response> updateInfoByEmptyAgeResponse = 내_회원_정보_수정_요청(사용자, null);
        비어있는_입력값으로_요청을_보냄(updateInfoByEmptyAgeResponse);

        ExtractableResponse<Response> updateInfoResponse = 내_회원_정보_수정_요청(사용자, NEW_AGE);
        회원_정보_수정됨(updateInfoResponse);

        ExtractableResponse<Response> updatePasswordResponseByFailPassword = 내_회원_비밀번호_수정_요청(사용자, PASSWORD + "Fail", NEW_PASSWORD);
        잘못된_내_비밀번호로_비밀번호_수정_요청함(updatePasswordResponseByFailPassword);

        ExtractableResponse<Response> updatePasswordResponseBySamePassword = 내_회원_비밀번호_수정_요청(사용자, PASSWORD, PASSWORD);
        내_비밀번호와_같은_비밀번호로_수정_요청함(updatePasswordResponseBySamePassword);

        ExtractableResponse<Response> updatePasswordResponseByEmptyPassword = 내_회원_비밀번호_수정_요청(사용자, "", NEW_PASSWORD);
        비어있는_입력값으로_요청을_보냄(updatePasswordResponseByEmptyPassword);

        ExtractableResponse<Response> updatePasswordResponseByEmptyNewPassword = 내_회원_비밀번호_수정_요청(사용자, PASSWORD, "");
        비어있는_입력값으로_요청을_보냄(updatePasswordResponseByEmptyNewPassword);

        ExtractableResponse<Response> updatePasswordResponse = 내_회원_비밀번호_수정_요청(사용자, PASSWORD, NEW_PASSWORD);
        회원_비밀번호_수정됨(updatePasswordResponse);

        ExtractableResponse<Response> deleteByStrangeTokenResponse = 잘못된_토큰으로_내_회원_삭제_요청(사용자);
        잘못된_토큰으로_요청을_보냄(deleteByStrangeTokenResponse);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자);
        회원_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 잘못된_토큰으로_내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "Strange")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static void 잘못된_입력값으로_회원가입_요청() {
        ExtractableResponse<Response> createEmptyEmailMemberResponse = 회원_생성을_요청(EMPTY_EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createEmptyPasswordMemberResponse = 회원_생성을_요청(NEW_EMAIL, EMPTY_PASSWORD, AGE);
        ExtractableResponse<Response> createErrorAgeMemberResponse = 회원_생성을_요청(NEW_EMAIL, PASSWORD, ERROR_AGE);
        ExtractableResponse<Response> createErrorEmailMemberResponse = 회원_생성을_요청(ERROR_EMAIL, PASSWORD, AGE);

        비어있는_입력값으로_요청을_보냄(createEmptyEmailMemberResponse);
        비어있는_입력값으로_요청을_보냄(createEmptyPasswordMemberResponse);
        비정상적인_나이_입력값으로_사용(createErrorAgeMemberResponse);
        잘못된_이메일로_회원가입(createErrorEmailMemberResponse);
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

    public static void 유효한_이메일로_중복체크(String email) {
        DuplicateEmailCheckRequest request = new DuplicateEmailCheckRequest(email);
        ExtractableResponse<Response> checkDuplicateResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().log().all().post("/api/members/exists")
                .then().log().all()
                .extract();

        assertThat(checkDuplicateResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 중복된_이메일로_중복체크(String email) {
        DuplicateEmailCheckRequest request = new DuplicateEmailCheckRequest(email);
        ExtractableResponse<Response> checkDuplicateResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/members/exists")
                .then().log().all()
                .extract();

        assertThat(checkDuplicateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse, Integer age) {
        MemberInfoUpdateRequest memberInfoUpdateRequest = new MemberInfoUpdateRequest(age);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberInfoUpdateRequest)
                .when().put("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_비밀번호_수정_요청(TokenResponse tokenResponse, String currentPassword, String newPassword) {
        MemberPasswordUpdateRequest memberPasswordUpdateRequest = new MemberPasswordUpdateRequest(currentPassword, newPassword);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberPasswordUpdateRequest)
                .when().put("/api/members/me/pw")
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

    public static ExtractableResponse<Response> 잘못된_토큰으로_내_회원_삭제_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "Strange")
                .when().delete("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 중복_회원을_생성(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("이미 가입된 이메일입니다");
        assertThat(exceptionResponse.getStatus()).isEqualTo(400);
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

    public static void 회원_비밀번호_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static TokenResponse 회원_로그인된_상태() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        return 로그인되어_있음(EMAIL, PASSWORD);
    }

    public static void 비어있는_입력값으로_요청을_보냄(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("입력되지 않은 항목을 확인해주세요");
    }

    private static void 잘못된_이메일로_회원가입(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("올바른 이메일 형식으로 입력해주세요");
    }

    public static void 비정상적인_나이_입력값으로_사용(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("1부터 150 사이의 나이를 입력해주세요");
    }

    private void 잘못된_내_비밀번호로_비밀번호_수정_요청함(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("현재 비밀번호를 다시 확인해주세요");
    }

    private void 내_비밀번호와_같은_비밀번호로_수정_요청함(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("현재 사용 중인 비밀번호입니다. 다른 비밀번호를 입력해주세요");
    }

    private void 잘못된_토큰으로_요청을_보냄(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("다시 로그인 후 시도해주세요");
    }
}
