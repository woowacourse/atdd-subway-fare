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
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "new_password";
    public static final int AGE = 20;
    public static final int NEW_AGE = 30;

    public static ExtractableResponse<Response> 회원_생성_요청(MemberRequest memberRequest) {
        회원_이메일_중복_확인(memberRequest.getEmail());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/api/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_이메일_중복_확인(String email) {
        MemberEmailRequest memberEmailRequest = new MemberEmailRequest(email);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberEmailRequest)
                .when().post("/api/members/exists")
                .then().log().all()
                .extract();
    }

    public static void 회원_생성_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청_비밀번호(TokenResponse tokenResponse,
                                                                   MemberPasswordRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/members/me/pw")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청_나이(TokenResponse tokenResponse,
                                                                 MemberAgeRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static void 내_회원_정보_수정_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 내_회원_삭제_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response, MemberRequest memberRequest) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        MemberResponse createMember = response.jsonPath().getObject(".", MemberResponse.class);

        assertThat(createMember)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(memberRequest);
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, Integer age) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberResponse memberResponse = response.as(MemberResponse.class);

        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨_비밀번호(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_정보_수정됨_나이(ExtractableResponse<Response> response, MemberAgeRequest param) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        MemberAgeResponse updatedMember = response.jsonPath().getObject(".", MemberAgeResponse.class);

        assertThat(updatedMember)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(param);
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse = 회원_생성_요청(memberRequest);
        회원_생성됨(createResponse, memberRequest);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        MemberPasswordRequest memberPasswordRequest = new MemberPasswordRequest(PASSWORD, NEW_PASSWORD);
        ExtractableResponse<Response> updatedPasswordResponse = 내_회원_정보_수정_요청_비밀번호(사용자, memberPasswordRequest);
        회원_정보_수정됨_비밀번호(updatedPasswordResponse);

        MemberAgeRequest memberAgeRequest = new MemberAgeRequest(NEW_AGE);
        ExtractableResponse<Response> updatedAgeResponse = 내_회원_정보_수정_요청_나이(사용자, memberAgeRequest);
        회원_정보_수정됨_나이(updatedAgeResponse, memberAgeRequest);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자);
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("회원 가입 - 중복된 이메일인 경우 400 에러가 발생한다.")
    @Test
    void createMemberDuplicateEmailException() {
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse = 회원_생성_요청(memberRequest);
        회원_생성됨(createResponse, memberRequest);

        ExtractableResponse<Response> response = 회원_생성_요청(memberRequest);

        회원_생성_요청_실패됨(response);
    }

    @DisplayName("회원 가입 - 이메일이 빈 값인 경우 400 에러가 발생한다.")
    @Test
    void createMemberBlankEmailException() {
        MemberRequest memberRequest = new MemberRequest(" ", PASSWORD, AGE);
        ExtractableResponse<Response> response = 회원_생성_요청(memberRequest);

        회원_생성_요청_실패됨(response);
    }

    @DisplayName("회원 가입 - 비밀번호가 빈 값인 경우 400 에러가 발생한다.")
    @Test
    void createMemberBlankPasswordException() {
        MemberRequest memberRequest = new MemberRequest(EMAIL, " ", AGE);
        ExtractableResponse<Response> response = 회원_생성_요청(memberRequest);

        회원_생성_요청_실패됨(response);
    }

    @DisplayName("회원 가입 - 나이가 1살 이상 150살 이하가 아닌 경우 400 에러가 발생한다.")
    @Test
    void createMemberAgeException() {
        MemberRequest underAgeRequest = new MemberRequest(EMAIL, PASSWORD, 0);
        ExtractableResponse<Response> underAgeResponse = 회원_생성_요청(underAgeRequest);

        회원_생성_요청_실패됨(underAgeResponse);

        MemberRequest overAgeRequest = new MemberRequest(EMAIL, PASSWORD, 151);
        ExtractableResponse<Response> overAgeResponse = 회원_생성_요청(overAgeRequest);

        회원_생성_요청_실패됨(overAgeResponse);
    }

    @DisplayName("회원 수정 - 현재 비밀번호가 틀린 경우 400 에러가 발생한다.")
    @Test
    void updateMemberInvalidPasswordException() {
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse = 회원_생성_요청(memberRequest);
        회원_생성됨(createResponse, memberRequest);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        MemberPasswordRequest memberPasswordRequest = new MemberPasswordRequest("1234", NEW_PASSWORD);
        ExtractableResponse<Response> response = 내_회원_정보_수정_요청_비밀번호(사용자, memberPasswordRequest);

        내_회원_정보_수정_실패됨(response);
    }

    @DisplayName("회원 수정 - 현재 비밀번호와 새로운 비밀번호가 같은 경우 400 에러가 발생한다.")
    @Test
    void updateMemberSamePasswordException() {
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse = 회원_생성_요청(memberRequest);
        회원_생성됨(createResponse, memberRequest);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        MemberPasswordRequest memberPasswordRequest = new MemberPasswordRequest(PASSWORD, PASSWORD);
        ExtractableResponse<Response> response = 내_회원_정보_수정_요청_비밀번호(사용자, memberPasswordRequest);

        내_회원_정보_수정_실패됨(response);
    }
}
