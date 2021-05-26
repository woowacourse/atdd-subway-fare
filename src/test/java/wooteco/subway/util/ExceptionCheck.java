package wooteco.subway.util;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import wooteco.subway.auth.ExceptionResponse;

public class ExceptionCheck {

    public static void 에러_문구_확인(ExtractableResponse<Response> response, String expectedError) {
        ExceptionResponse responseData = response.as(ExceptionResponse.class);
        assertThat(responseData.getError()).isEqualTo(expectedError);
    }

    public static void 코드_400_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
