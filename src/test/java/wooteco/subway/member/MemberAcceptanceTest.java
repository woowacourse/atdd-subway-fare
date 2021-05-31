package wooteco.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.EmailRequest;
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

    @DisplayName("동일한 이메일이 있는지 확인한다.")
    @Test
    void checkDuplicatedEmail() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> checkResponse = 중복된_이메일_확인(new EmailRequest(EMAIL));
        중복된_이메일임(checkResponse);
    }

    @DisplayName("동일한 이메일로 회원을 생성할 수 없다.")
    @Test
    void createMemberWithDuplicatedException() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        회원_생성_실패됨_CONFLICT(createResponse);
    }

    @DisplayName("이메일 입력값은 이메일 형식을 갖춰야 하며, 빈 문자열, null, 공백이 아닌 4글자 이상 20글자 이하여야 한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "123", "1@1", "123213213213213211233@email.com"})
    void create_email(String email) {
        final MemberRequest memberRequest = new MemberRequest(email, "test", 20);
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(memberRequest);
        회원_생성_실패됨_BAD_REQUEST(createResponse);
    }

    @DisplayName("패스워드 입력값은 빈 문자열, null, 공백이 아닌 4글자 이상 20글자 이하여야 한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "123", "가나@email.com", "1@1", "123213213213213211233@email.com"})
    void create_password(String password) {
        final MemberRequest memberRequest = new MemberRequest("email@email.com", password, 20);
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(memberRequest);
        회원_생성_실패됨_BAD_REQUEST(createResponse);
    }

    @DisplayName("나이 입력값은 양수여야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void create_age(int age) {
        final MemberRequest memberRequest = new MemberRequest("email@email.com", "test" , age);
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(memberRequest);
        회원_생성_실패됨_BAD_REQUEST(createResponse);
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

    public static ExtractableResponse<Response> 회원_생성을_요청(MemberRequest memberRequest) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/members")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 중복된_이메일_확인(EmailRequest emailRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(emailRequest)
            .when().post("/members/email-check")
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
    public static void 회원_생성_실패됨_CONFLICT(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 회원_생성_실패됨_BAD_REQUEST(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 중복된_이메일임(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
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
