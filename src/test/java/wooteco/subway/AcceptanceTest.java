package wooteco.subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.member.dto.MemberRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        사용자_등록_요청();
    }

    private void 사용자_등록_요청() {
        MemberRequest memberRequest = new MemberRequest(
            "test@email.com",
            "password",
            10
        );
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/api/members")
            .then().log().all();
    }

    public static String 토큰_요청() {
        TokenRequest tokenRequest = new TokenRequest(
            "test@email.com",
            "password"
        );
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/api/login/token")
            .then().log().all()
            .extract()
            .body().jsonPath().getString("accessToken");
    }
}
