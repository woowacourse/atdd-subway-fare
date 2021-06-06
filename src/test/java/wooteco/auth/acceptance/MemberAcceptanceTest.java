package wooteco.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.AcceptanceTest;
import wooteco.auth.web.dto.request.MemberRequest;
import wooteco.auth.web.dto.response.MemberResponse;
import wooteco.auth.web.dto.response.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.auth.acceptance.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.utils.RestDocsUtils.*;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "new_email@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int NEW_AGE = 30;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE, "member-create");
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자, "members-findme");
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(사용자, EMAIL, NEW_PASSWORD, NEW_AGE, "members-updateme");
        회원_정보_수정됨(updateResponse);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자, "members-deleteme");
        회원_삭제됨(deleteResponse);
    }

    @Test
    @DisplayName("유저 중복 확인 - 중복 존재")
    public void duplicateMember() {
        //given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        //when
        ExtractableResponse<Response> checkDuplicateResponse = 유저_중복_확인(EMAIL, "member-duplicate");

        //then
        중복_회원_존재함(checkDuplicateResponse);
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

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age, String documentIdentifier) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given(getRequestSpecification()).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().post("/api/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse, String documentIdentifier) {
        return RestAssured
                .given(getRequestSpecification()).log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().get("/api/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse, String email, String password, Integer age, String documentIdentifier) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
                .given(getRequestSpecification()).log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().put("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_삭제_요청(TokenResponse tokenResponse, String documentIdentifier) {
        return RestAssured
                .given(getRequestSpecification()).log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().delete("/api/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 유저_중복_확인(String email, String documentIdentifier) {
        return RestAssured
                .given(getRequestSpecification()).log().all()
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().get("/api/members?email=" + email)
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

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 중복_회원_존재함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(Boolean.class)).isEqualTo(true);
    }
}
