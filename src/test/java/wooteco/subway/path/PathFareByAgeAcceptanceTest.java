package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회 - 노선에 따른 추가요금")
public class PathFareByAgeAcceptanceTest extends AcceptanceTest {
    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;

    private StationResponse a역;
    private StationResponse b역;
    private StationResponse c역;
    private StationResponse d역;
    private StationResponse e역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        a역 = 지하철역_등록되어_있음("a역");
        b역 = 지하철역_등록되어_있음("b역");

        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-red-600", 0, a역, b역, 1);
    }

    @DisplayName("로그인 되지 않은 사용자인 경우 - 기본 운임 요금")
    @Test
    void findFareByLine1() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, b역));
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("로그인 된 사용자인 경우 & 어린이인 경우 - 운임에서 350원을 공제한 금액의 50%할인")
    @Test
    void findFareByLine2() {
        // given
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        Integer AGE = 7;
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청_with_토큰(1L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, b역));
        총_요금이_응답됨(response,  (int) ((1250 - 350) * 0.5));
    }

    @DisplayName("로그인 된 사용자인 경우 & 청소년인 경우 - 운임에서 350원을 공제한 금액의 20%할인")
    @Test
    void findFareByLine3() {
        //when
        // given
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        Integer AGE = 15;
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 거리_경로_조회_요청_with_토큰(1L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, b역));
        총_요금이_응답됨(response, (int) ((1250 - 350) * 0.8));
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청_with_토큰(long source, long target, TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    public static void 적절한_경로_응답됨(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 총_요금이_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    public static TokenResponse 로그인되어_있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        return response.as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(params).
            when().
            post("/login/token").
            then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract();
    }

    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, Integer age) {
        return 회원_생성을_요청(email, password, age);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/members")
            .then().log().all()
            .extract();
    }
}
