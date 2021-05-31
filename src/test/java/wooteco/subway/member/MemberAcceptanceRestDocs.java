package wooteco.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.RestDocs;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.member.dto.MemberRequest;

import static wooteco.subway.DocsIdentifier.*;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.member.MemberAcceptanceTest.*;

public class MemberAcceptanceRestDocs extends RestDocs {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    public static final String NEW_EMAIL = "new_email@email.com";
    public static final String NEW_PASSWORD = "new_password";
    public static final int NEW_AGE = 30;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE, MEMBERS_POST_SUCCESS);
        회원_생성됨(createResponse);

        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(사용자);
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        ExtractableResponse<Response> updateResponse = 내_회원_정보_수정_요청(사용자, EMAIL, NEW_PASSWORD, NEW_AGE, MEMBERS_ME_PUT_SUCCESS);
        회원_정보_수정됨(updateResponse);

        ExtractableResponse<Response> deleteResponse = 내_회원_삭제_요청(사용자);
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("이메일 전 글자수는 30자를 넘을 수 없다.")
    @Test
    void createMemberWhenEmailOverLength() throws Exception {
        //given
        String overEmail = "abcdefghijklnmopqrstuvwxyz123dsfkldafskldasfkladfskjl456789@google.com";

        //when
        ExtractableResponse<Response> createFailResponse = 회원_생성을_요청(overEmail, PASSWORD, AGE, MEMBERS_POST_FAIL_EMAIL);

        //then
        회원_생성되지않음(createFailResponse);

        //given
        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        //when
        createFailResponse = 내_회원_정보_수정_요청(사용자, overEmail, PASSWORD, AGE, MEMBERS_ME_PUT_FAIL_EMAIL);

        //then
        회원_수정되지않음(createFailResponse);

    }

    @DisplayName("비밀번호는 4글자 이상 20글자 이하이어야 한다.")
    @Test
    void createMemberWhenErrorPassword () throws Exception {
        //given
        String shortPassword = "abc";
        String longPassword = "abcdefghijklnmopqrstuvwsadsfkasdfdsafjdfsljdsflkasdfjlxyz";

        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> createFailResponse = 회원_생성을_요청(EMAIL, shortPassword, AGE, MEMBERS_POST_FAIL_PASSWORD);

        //then
        회원_생성되지않음(createFailResponse);

        //when
        createFailResponse = 내_회원_정보_수정_요청(사용자, EMAIL, shortPassword, AGE, MEMBERS_ME_PUT_FAIL_PASSWORD);

        //then
        회원_수정되지않음(createFailResponse);

        //when
        createFailResponse = 회원_생성을_요청(EMAIL, longPassword, AGE, MEMBERS_POST_FAIL_PASSWORD);

        //then
        회원_생성되지않음(createFailResponse);

        //when
        createFailResponse = 내_회원_정보_수정_요청(사용자, EMAIL, longPassword, AGE, MEMBERS_ME_PUT_FAIL_PASSWORD);

        //then
        회원_수정되지않음(createFailResponse);
    }

    @DisplayName("나이는 음수일 수 없으며 200살을 넘을 수 없다.")
    @Test
    void createMemberErrorAge () throws Exception {
        //given
        int negativeAge = -1;
        int exceedAge = 201;

        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        TokenResponse 사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> createFailResponse = 회원_생성을_요청(EMAIL, PASSWORD, negativeAge, MEMBERS_POST_FAIL_AGE);

        //then
        회원_생성되지않음(createFailResponse);

        //when
        createFailResponse = 내_회원_정보_수정_요청(사용자, EMAIL, PASSWORD, negativeAge, MEMBERS_ME_PUT_FAIL_AGE);

        //then
        회원_수정되지않음(createFailResponse);


        //when
        createFailResponse = 회원_생성을_요청(EMAIL, PASSWORD, exceedAge, MEMBERS_POST_FAIL_AGE);

        //then
        회원_생성되지않음(createFailResponse);

        //when
        createFailResponse = 내_회원_정보_수정_요청(사용자, EMAIL, PASSWORD, exceedAge, MEMBERS_ME_PUT_FAIL_AGE);

        //then
        회원_수정되지않음(createFailResponse);

    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age, String identifier) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return given(identifier)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return given(MEMBERS_ME_GET)
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_수정_요청(TokenResponse tokenResponse, String email, String password, Integer age, String identifier) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return given(identifier)
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_삭제_요청(TokenResponse tokenResponse) {
        return given(MEMBERS_ME_DELETE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }
}