package wooteco.subway.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인_성공_후_토큰추출;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@DisplayName("회원 정보 관련 기능")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final int AGE = 20;
    public static final String PASSWORD = "password";
    public static final int NEW_AGE = 30;
    public static final String NEW_PASSWORD = "new_password";

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_가입_요청(EMAIL, PASSWORD, AGE, "sign-up");
        응답코드_CREATED_확인(createResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD, "login");
        응답코드_확인(loginResponse, HttpStatus.OK);
        TokenResponse tokenResponse = 로그인_성공_후_토큰추출(loginResponse);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(tokenResponse, "get-login-user-my-info");
        응답코드_확인(findResponse, HttpStatus.OK);
        조회된_회원_정보_일치_확인(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(tokenResponse, EMAIL, NEW_PASSWORD, NEW_AGE, "update-my-info");
        응답코드_확인(updateResponse, HttpStatus.OK);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(tokenResponse, "delete-my-account");
        응답코드_확인(deleteResponse, HttpStatus.NO_CONTENT);
    }

    @DisplayName("이메일 중복 에러")
    @Test
    void signUpEmailDuplicateException() {
        // given
        ExtractableResponse<Response> createdResponse = 회원_가입_요청(EMAIL, PASSWORD, AGE, "sign-up");
        응답코드_CREATED_확인(createdResponse);

        // when
        ExtractableResponse<Response> createBadRequestResponse = 회원_가입_요청(EMAIL, PASSWORD, AGE, "sign-up-email-duplicate-fail");

        // then
        응답코드_확인(createBadRequestResponse, HttpStatus.BAD_REQUEST);
    }

    public static ExtractableResponse<Response> 회원_가입_요청(String email, String password, Integer age, String docsIdentifier) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured
            .given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .body(memberRequest)
            .when().post("/members")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse, String docsIdentifier) {
        return RestAssured.given(spec).log().all().
            auth().oauth2(tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON_VALUE).
            filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()))).
            when().
            get("/members/me").
            then().
            log().all().
            extract();
    }

    public static void 조회된_회원_정보_일치_확인(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse, String email, String password, Integer age, String docsIdentifier) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured
            .given(spec).log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .body(memberRequest)
            .when().put("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 내_회원_삭제_요청(TokenResponse tokenResponse, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().delete("/members/me")
            .then().log().all()
            .extract();
    }

    public static void 응답코드_확인(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    public static void 응답코드_CREATED_확인(ExtractableResponse<Response> signUpResponse) {
        응답코드_확인(signUpResponse, HttpStatus.CREATED);
        assertThat(signUpResponse.header("Location")).isNotBlank();
    }
}
