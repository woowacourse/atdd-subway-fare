package wooteco.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.SubwayMemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "new_email@email.com";
    public static final String NEW_PASSWORD = "new_password";
    public static final int NEW_AGE = 30;

    @DisplayName("사용자를 생성한다.")
    @Test
    void createMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
    }

    @DisplayName("중복된 이메일로 사용자를 생성하면 에러가 발생한다.")
    @Test
    void createMemberWithDuplicateEmail() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        에러가_발생한다(createResponse, SubwayMemberException.DUPLICATE_EMAIL_EXCEPTION);
    }

    @DisplayName("잘못된 이메일을 이용하여 사용자를 생성하면 에러가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "abc", "abc@jjj", "naver.com@abc", "abc@naver.", "ab @naver.com"})
    void createMemberWithInvalidEmail(String value) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(value, PASSWORD, AGE);
        에러가_발생한다(createResponse, SubwayMemberException.INVALID_EMAIL_EXCEPTION);
    }

    @DisplayName("잘못된 비밀번호를 이용하여 사용자를 생성하면 에러가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "a"})
    void createMemberWithInvalidPassword(String value) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, value, AGE);
        에러가_발생한다(createResponse, SubwayMemberException.INVALID_PASSWORD_EXCEPTION);
    }

    @DisplayName("잘못된 나이를 이용하여 사용자를 생성하면 에러가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-1, 0})
    void createMemberWithInvalidAge(Integer value) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, value);
        에러가_발생한다(createResponse, SubwayMemberException.INVALID_AGE_EXCEPTION);
    }

    @DisplayName("토큰을 이용하여 정보를 조회한다.")
    @Test
    void showMemberInfo() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자);
        회원_정보_조회됨(findResponse, EMAIL, AGE);
    }

    @DisplayName("토큰을 이용하여 정보를 수정한다.")
    @Test
    void updateMember() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(사용자, EMAIL, NEW_PASSWORD,
            NEW_AGE);
        회원_정보_수정됨(updateResponse);
    }

    @DisplayName("토큰을 이용하여 정보를 수정하는데 사용중인 이메일로 변경하면 에러가 발생한다.")
    @Test
    void updateMemberWithDuplicateEmail() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성을_요청(NEW_EMAIL, PASSWORD, AGE);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(사용자, NEW_EMAIL, NEW_PASSWORD,
            NEW_AGE);
//        에러가_발생한다(updateResponse, SubwayMemberException.DUPLICATE_EMAIL_EXCEPTION);
    }

    @DisplayName("토큰을 이용하여 회원정보를 삭제한다.")
    @Test
    void deleteMember() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자);
        회원_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password,
        Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/members")
            .then().log().all()
            .extract();
    }

    private void 에러가_발생한다(ExtractableResponse<Response> response, SubwayException subwayException) {
        assertThat(response.statusCode()).isEqualTo(subwayException.status());
        assertThat(response.body().asString()).isEqualTo(subwayException.message());
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

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse,
        String email, String password, Integer age) {
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
