package wooteco.subway.util;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.member.MemberAcceptanceTest.AGE;
import static wooteco.subway.member.MemberAcceptanceTest.EMAIL;
import static wooteco.subway.member.MemberAcceptanceTest.PASSWORD;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import wooteco.subway.ExceptionResponse;
import wooteco.subway.auth.dto.TokenResponse;

public class ExceptionCheck {

    private static final TokenResponse tokenResponse;

    static {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);
    }

    public static void 에러_문구_확인(ExtractableResponse<Response> response, String expectedError) {
        ExceptionResponse responseData = response.as(ExceptionResponse.class);
        assertThat(responseData.getError()).isEqualTo(expectedError);
    }

    public static void 코드_400_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static String getDefaultToken() {
        return tokenResponse.getAccessToken();
    }
}
