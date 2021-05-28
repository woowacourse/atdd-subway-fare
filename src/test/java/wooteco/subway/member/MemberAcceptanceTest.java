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
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.util.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.util.TestUtil.*;

public class MemberAcceptanceTest extends AcceptanceTest {

    private String loginToken;

    public static ExtractableResponse<Response> getMyMemberInfo(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> editMemberInfo(MemberRequest memberRequest, String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().put("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteMember(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().delete("/members/me")
                .then().log().all()
                .extract();
    }

    private void assertUserInformation(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse member = response.as(MemberResponse.class);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getAge()).isEqualTo(age);
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        registerMember("kevin@naver.com", "123", 123);
        loginToken = TestUtil.login("kevin@naver.com", "123")
                .getAccessToken();
    }

    @DisplayName("회원 생성시 중복된 이메일로 생성할 수 없다.")
    @Test
    void cannotRegisterMemberAsDuplicated() {
        ExtractableResponse<Response> response = registerMember("kevin@naver.com", "123", 123);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "중복된 이메일입니다.");
    }

    @DisplayName("회원 정보를 확인한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> memberInfoResponse = getMyMemberInfo(loginToken);

        assertResponseStatus(memberInfoResponse, HttpStatus.OK);
        assertUserInformation(memberInfoResponse, "kevin@naver.com", 123);
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void editMember() {
        MemberRequest memberRequest = new MemberRequest("kevin@naver.com", "newpass", 10);

        ExtractableResponse<Response> response = editMemberInfo(memberRequest, loginToken);

        assertResponseStatus(response, HttpStatus.OK);
    }

    @DisplayName("회원 정보 수정시 중복된 이메일로 수정할 수 없다.")
    @Test
    void cannotEditMemberWhenDuplicatedEmail() {
        registerMember("dup@naver.com", "abc", 10);
        MemberRequest memberRequest = new MemberRequest("dup@naver.com", "newpass", 10);

        ExtractableResponse<Response> response = editMemberInfo(memberRequest, loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "중복된 이메일입니다.");
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        ExtractableResponse<Response> response = deleteMember(loginToken);

        assertResponseStatus(response, HttpStatus.NO_CONTENT);
    }

    @DisplayName("가입 요청시 나이는 1 이상이어야 한다.")
    @Test
    void ageMustBeOverOne() {
        ExtractableResponse<Response> response = registerMember("agb@naver.com", "312", 0);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "잘못된 나이입니다.");
    }
}
