package wooteco.subway.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.utils.DocumentUtils;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.utils.DocumentUtils.getRequestPreprocessor;
import static wooteco.utils.DocumentUtils.getResponsePreprocessor;

public class AuthRestAssuredRequestUtils {
    public static ExtractableResponse<Response> 로그인_요청(String email, String password, String documentIdentifier) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured
                .given(DocumentUtils.getRequestSpecification())
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when()
                .post("/login/token")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse, String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification())
                .log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
