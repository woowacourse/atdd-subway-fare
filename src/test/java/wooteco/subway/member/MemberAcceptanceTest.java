package wooteco.subway.member;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.ui.FindFailEmailResponse;
import wooteco.subway.util.ExceptionCheck;

public class MemberAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "new_email@email.com";
    public static final String NEW_PASSWORD = "new_password";
    public static final int NEW_AGE = 30;

    private static ExtractableResponse<Response> 회원이_존재하는지_요청(String email) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(ExceptionCheck.getDefaultToken())
            .when().get("/api/members/check-validation?email=" + email)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password,
        Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/api/members")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 잘못된_회원_생성을_요청(String email, String password,
        String age) {
        Map<String, String> invalidParam = new HashMap<>();
        invalidParam.put("email", email);
        invalidParam.put("password", password);
        invalidParam.put("age", age);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(invalidParam)
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

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse,
        String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
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

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_존재_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_존재_없음_응답됨(ExtractableResponse<Response> response) {
        FindFailEmailResponse findFailEmailResponse = response.as(FindFailEmailResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        System.out.println(findFailEmailResponse.getTimestamp());
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(사용자, EMAIL, NEW_PASSWORD,
            NEW_AGE);
        회원_정보_수정됨(updateResponse);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자);
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("회원이 존재하는지를 확인한다")
    @Test
    void existMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> response = 회원이_존재하는지_요청(EMAIL);
        회원_존재_응답됨(response);
    }

    @DisplayName("회원이 존재하지 않을 시")
    @Test
    void noExistMember() {

        ExtractableResponse<Response> response = 회원이_존재하는지_요청(EMAIL);
        회원_존재_없음_응답됨(response);
    }

    @DisplayName("잘못된 나이가 입력되는 경우")
    @Test
    void ageIsNotDigit() {
        ExtractableResponse<Response> createResponse = 잘못된_회원_생성을_요청(EMAIL, PASSWORD, "TEST");

        ExceptionCheck.코드_400_응답됨(createResponse);
        ExceptionCheck.에러_문구_확인(createResponse, "INVALID_AGE");
    }

    @DisplayName("null 이 입력되는 경우")
    @Test
    void createHaveNull() {
        ExtractableResponse<Response> createResponse = 잘못된_회원_생성을_요청(EMAIL, null, null);

        ExceptionCheck.코드_400_응답됨(createResponse);
        ExceptionCheck.에러_문구_확인(createResponse, "INVALID_INPUT");
    }

    @DisplayName("중복된 ID가 입력되는 경우")
    @Test
    void duplicateId() {
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> createResponse2 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExceptionCheck.코드_400_응답됨(createResponse2);
        ExceptionCheck.에러_문구_확인(createResponse2, "DUPLICATED_ID");

    }
}
