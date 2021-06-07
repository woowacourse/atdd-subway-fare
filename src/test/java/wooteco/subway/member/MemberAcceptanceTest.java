package wooteco.subway.member;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

public class MemberAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_PASSWORD = "new_password";
    public static final int NEW_AGE = 30;
    public static final String UNIQUE_EMAIL = "unique@email.com";

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

    @DisplayName("회원 생성에 실패한다. - 이메일 형식이 아님")
    @Test
    void failPostMember_wrongEmail() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청("wrongEmail", PASSWORD, AGE);
        회원_생성에_실패함(createResponse);
    }

    @DisplayName("회원 생성에 실패한다. - 비어있거나 null인 이메일")
    @NullAndEmptySource
    @ParameterizedTest
    void failPostMember_emptyEmail(String email) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(email, PASSWORD, AGE);
        회원_생성에_실패함(createResponse);
    }

    @DisplayName("회원 생성에 실패한다. - 비어있거나 null인 비밀번호")
    @NullAndEmptySource
    @ParameterizedTest
    void failPostMember_emptyPassword(String password) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, password, AGE);
        회원_생성에_실패함(createResponse);
    }

    @DisplayName("회원 생성에 실패한다. - 음수인 나이")
    @Test
    void failPostMember_wrongAge() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, -1);
        회원_생성에_실패함(createResponse);
    }

    @DisplayName("회원 생성에 실패한다. - null인 나이")
    @NullSource
    @ParameterizedTest
    void failPostMember_emptyAge(Integer age) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, age);
        회원_생성에_실패함(createResponse);
    }

    @DisplayName("회원 조회 실패 - 유효하지 않은 토큰")
    @Test
    void failToFindMember() {
        TokenResponse tokenResponse = new TokenResponse("wrongAccesstoken");

        RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("이메일의 중복여부를 검사한다.")
    @Test
    void verifyUniqueEmail() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        이메일_중복_검사_요청(EMAIL, HttpStatus.CONFLICT);
        이메일_중복_검사_요청(UNIQUE_EMAIL, HttpStatus.NO_CONTENT);
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

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse, String email,
        String password, Integer age) {
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

    public static void 회원_생성에_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static ExtractableResponse<Response> 이메일_중복_검사_요청(String email, HttpStatus expectedHttpStatus) {
        EmailRequest emailRequest = new EmailRequest(email);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(emailRequest)
            .when().post("/members/email-check")
            .then().log().all()
            .statusCode(expectedHttpStatus.value())
            .extract();
    }
}
