package wooteco.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.ErrorResponse;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "new_email@email.com";
    public static final String NEW_PASSWORD = "new_password";
    public static final int NEW_AGE = 30;

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

    @DisplayName("회원 가입 시 Email 형식이 유효해야 한다.")
    @Test
    void createMemberWithValidEmail() {
        String invalidEmail1 = "sdklfjsdlf";
        ExtractableResponse<Response> response1 = 회원_생성을_요청(invalidEmail1, "password", 20);
        회원_가입_실패(response1);

        String invalidEmail2 = "kokoko@";
        ExtractableResponse<Response> response2 = 회원_생성을_요청(invalidEmail2, "password", 20);
        회원_가입_실패(response2);

        String invalidEmail3 = "@com";
        ExtractableResponse<Response> response3 = 회원_생성을_요청(invalidEmail3, "password", 20);
        회원_가입_실패(response3);
    }

    @DisplayName("회원 가입 시 나이는 양수여야 한다.")
    @Test
    void createMemberWithValidAge() {
        int AGE = -10;
        ExtractableResponse<Response> response = 회원_생성을_요청("hello@hello.com", "password", AGE);
        회원_가입_실패(response);
    }

    @DisplayName("회원 가입 시 비밀번호는 영어/숫자로 이루어진 1자 이상이어야 한다.")
    @Test
    void createMemberWithValidPassword() {
        String blankAsPassword = "";
        ExtractableResponse<Response> response1 = 회원_생성을_요청("hello@hello.com", blankAsPassword, 10);
        회원_가입_실패(response1);

        String koreanAsPassword = "한글비밀번호";
        ExtractableResponse<Response> response2 = 회원_생성을_요청("hello@hello.com", koreanAsPassword, 10);
        회원_가입_실패(response2);

        String specialCharacterAsPassword = "%%##$";
        ExtractableResponse<Response> response3 = 회원_생성을_요청("hello@hello.com", specialCharacterAsPassword, 10);
        회원_가입_실패(response3);
    }

    @DisplayName("이미 가입된 이메일로는 다시 가입할 수 없다.")
    @Test
    void createMemberWithDuplicateEmail() {
        final String duplicateEmail = "hello@hello.com";
        회원_생성을_요청(duplicateEmail, "password", 10);
        ExtractableResponse<Response> response = 회원_생성을_요청(duplicateEmail, "password", 10);
        회원_가입_이메일_중복(response);
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

    public static void 회원_가입_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("양식에 맞지 않는 회원가입 요청입니다.");
    }

    public static void 회원_가입_이메일_중복(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("이미 가입된 이메일입니다.");
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
}
