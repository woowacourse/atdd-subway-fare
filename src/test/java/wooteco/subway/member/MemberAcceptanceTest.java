package wooteco.subway.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.member.dto.*;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;

public class MemberAcceptanceTest extends AcceptanceTest {

    private TokenResponse 신규회원;
    private String newEmail;

    @BeforeEach
    void beforeEach() {
        newEmail = "new@new.com";
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(newEmail, PASSWORD, AGE);
        신규_회원_생성됨(createResponse);

        신규회원 = 로그인되어_있음(newEmail, PASSWORD);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(신규회원);

        회원_삭제됨(deleteResponse);
    }

    @Test
    @DisplayName("이미 가입된 메일로 회원 가입을 요청하는 경우 예외를 던진다.")
    void testJoinMemberWhenEmailAlreadyExists() {
        ExtractableResponse<Response> 중복회원 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        assertThat(중복회원.as(ExceptionResponse.class).getMessage()).isEqualTo("이미 가입된 이메일입니다");
    }

    @Test
    @DisplayName("내 회원 정보를 조회한다.")
    void testGetMemberInfo() {
        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(신규회원);

        신규회원_정보_조회됨(findResponse, newEmail, AGE);
    }

    @Test
    @DisplayName("비밀번호를 수정한다.")
    void testChangePassword() {
        ExtractableResponse<Response> updatePwResponse = 내_비밀번호_수정_요청(신규회원, PASSWORD, NEW_PASSWORD);

        회원_정보_수정됨(updatePwResponse);
    }

    @Test
    @DisplayName("현재 비밀번호를 잘 못 입력한 경우 예외를 던진다.")
    void testChangePasswordWhenInvalidPassword() {
        ExtractableResponse<Response> notValidPwRes = 내_비밀번호_수정_요청(신규회원, PASSWORD + "!", NEW_PASSWORD);

        assertThat(notValidPwRes.as(ExceptionResponse.class).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("수정하려는 비밀번호가 현재 비밀번호와 같은 경우 예외를 던진다.")
    void testChangePasswordWhenSameWithCurrentPassword() {
        ExtractableResponse<Response> samePwRes = 내_비밀번호_수정_요청(신규회원, PASSWORD, PASSWORD);

        assertThat(samePwRes.as(ExceptionResponse.class).getMessage()).isEqualTo("현재 사용 중인 비밀번호입니다. 다른 비밀번호를 입력해주세요");
    }

    @Test
    @DisplayName("회원의 나이 정보를 수정한다.")
    void testChangeMembersAge() {
        ExtractableResponse<Response> updateAgeResponse = 내_나이정보_수정_요청(신규회원, NEW_AGE);
        나이_정보_수정됨(updateAgeResponse);
    }

    @Test
    @DisplayName("수정하려는 나이가 유효한 범위의 값이 아닌 경우 예외를 던진다.")
    void testChangeMembersAgeWhenNotValidRequest() {
        ExtractableResponse<Response> 최소나이 = 내_나이정보_수정_요청(신규회원, 0);
        ExtractableResponse<Response> 최대나이 = 내_나이정보_수정_요청(신규회원, 151);


        assertThat(최소나이.as(ExceptionResponse.class).getMessage()).isEqualTo("1부터 150 사이의 나이를 입력해주세요");
        assertThat(최대나이.as(ExceptionResponse.class).getMessage()).isEqualTo("1부터 150 사이의 나이를 입력해주세요");
    }

    @Test
    @DisplayName("중복된 이메일이 존재하면 예외를 던진다.")
    void testCheckExistsEmail() {
        String Email = "new@new.com";
        ExtractableResponse<Response> findResponse = 이메일_중복_확인(Email);

        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("중복된 이메일이 존재하지 않는 경우 응답코드 NO_CONTENT.")
    void testCheckExistsEmailWhenNotExists() {
        String Email = "new2@new.com";

        ExtractableResponse<Response> findResponse = 이메일_중복_확인(Email);

        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("요청에 빈 값이 있는 경우 예외를 던진다.")
    void testWhenRequestIsEmpty() {
        // given
        String Email = "";
        String password = "";
        Integer age = null;

        // when
        ExtractableResponse<Response> response = 회원_생성을_요청(Email, password, age);

        // then
        ExceptionResponse as = response.as(ExceptionResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(as.getMessage()).isEqualTo("입력되지 않은 항목을 확인해주세요");
    }

    @Test
    @DisplayName("나이가 유효하지 않은 경우")
    void testWhenInvalidAgeRequest() {
        // given
        String Email = "asd@asd.com";
        String password = "asd";
        Integer age = 0;
        Integer age2 = 151;

        // when
        ExtractableResponse<Response> response = 회원_생성을_요청(Email, password, age);
        ExtractableResponse<Response> response2 = 회원_생성을_요청(Email, password, age2);

        ExceptionResponse exception = response.as(ExceptionResponse.class);
        ExceptionResponse exception2 = response2.as(ExceptionResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        assertThat(exception.getMessage()).isEqualTo("1부터 150 사이의 나이를 입력해주세요");
        assertThat(exception2.getMessage()).isEqualTo("1부터 150 사이의 나이를 입력해주세요");
    }

    @Test
    @DisplayName("이메일 형식이 유효하지 않은 경우")
    void testWhenInvalidEmailForm() {
        // given
        String Email = "123";
        String password = "asd";
        Integer age = 30;

        // when
        ExtractableResponse<Response> response = 회원_생성을_요청(Email, password, age);

        ExceptionResponse exception = response.as(ExceptionResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exception.getMessage()).isEqualTo("올바른 이메일 형식으로 입력해주세요");
    }

    private ExtractableResponse<Response> 이메일_중복_확인(String email) {
        EmailCheckRequest request = new EmailCheckRequest(email);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/members/exists")
                .then().log().all()
                .extract();
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

    public static ExtractableResponse<Response> 내_비밀번호_수정_요청(TokenResponse tokenResponse, String password, String newPassword) {
        PasswordRequest passwordRequest = new PasswordRequest(password, newPassword);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(passwordRequest)
                .when().put("/api/members/me/pw")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 내_나이정보_수정_요청(TokenResponse tokenResponse, int newAge) {
        AgeRequest ageRequest = new AgeRequest(newAge);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ageRequest)
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
        assertThat(response.header("Location")).isEqualTo("/members/1");
    }

    public static void 신규_회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/members/2");
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    private void 신규회원_정보_조회됨(ExtractableResponse<Response> findResponse, String email, int age) {
        MemberResponse memberResponse = findResponse.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isEqualTo(2);
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 나이_정보_수정됨(ExtractableResponse<Response> updateAgeResponse) {
        assertThat(updateAgeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateAgeResponse.as(AgeResponse.class).getId()).isEqualTo(2);
        assertThat(updateAgeResponse.as(AgeResponse.class).getAge()).isEqualTo(NEW_AGE);
    }
}
