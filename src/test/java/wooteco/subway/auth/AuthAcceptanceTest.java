package wooteco.subway.auth;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.subway.member.MemberAcceptanceTest.응답코드_확인;
import static wooteco.subway.member.MemberAcceptanceTest.조회된_회원_정보_일치_확인;
import static wooteco.subway.member.MemberAcceptanceTest.회원_가입_요청;

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

@DisplayName("회원 인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // given
        ExtractableResponse<Response> signUpResponse = 회원_가입_요청(EMAIL, PASSWORD, AGE, "sign-up");
        응답코드_확인(signUpResponse, HttpStatus.CREATED);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD, "login");
        응답코드_확인(loginResponse, HttpStatus.OK);
        TokenResponse tokenResponse = 로그인_성공_후_토큰추출(loginResponse);

        // when
        ExtractableResponse<Response> myInfoResponse = 내_회원_정보_조회_요청(tokenResponse, "get-login-user-my-info");

        // then
        응답코드_확인(loginResponse, HttpStatus.OK);
        조회된_회원_정보_일치_확인(myInfoResponse, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // given
        ExtractableResponse<Response> signUpResponse = 회원_가입_요청(EMAIL, PASSWORD, AGE, "sign-up");
        응답코드_확인(signUpResponse, HttpStatus.CREATED);

        // when
        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL + "OTHER", PASSWORD, "login-fail");

        // then
        응답코드_확인(loginResponse, HttpStatus.UNAUTHORIZED);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // given
        TokenResponse tokenResponse = new TokenResponse("accesstoken");

        // when
        ExtractableResponse<Response> myInfoResponse = 내_회원_정보_조회_요청(tokenResponse, "not-valid-login-token-when-get-login-user-my-info");

        // then
        응답코드_확인(myInfoResponse, HttpStatus.UNAUTHORIZED);
    }

    public static TokenResponse 로그인되어_있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password, "login");
        return response.as(TokenResponse.class);
    }

    public static TokenResponse 로그인_성공_후_토큰추출(ExtractableResponse<Response> response) {
        return response.as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password, String docsIdentifier) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given(spec).log().all().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(params).
            filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()))).
            when().
            post("/login/token").
            then().
            log().all().
            extract();
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
}
