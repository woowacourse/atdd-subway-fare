package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.ui.dto.TokenRequest;
import wooteco.subway.auth.ui.dto.TokenResponse;
import wooteco.subway.line.ui.dto.LineResponse;
import wooteco.subway.member.ui.dto.MemberRequest;
import wooteco.subway.member.ui.dto.MemberResponse;
import wooteco.subway.path.ui.dto.PathResponse;
import wooteco.subway.station.ui.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final String PASSWORD = "abcd";
    private MemberRequest 어린이;
    private MemberRequest 청소년;
    private MemberRequest 성인;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        어린이 = new MemberRequest("abc1@abc.abc", PASSWORD, 6);
        청소년 = new MemberRequest("abc2@abc.abc", "abcd", 13);
        성인 = new MemberRequest("abc3@abc.abc", "abcd", 19);

        회원_가입되어_있음(어린이);
        회원_가입되어_있음(청소년);
        회원_가입되어_있음(성인);

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

<<<<<<< HEAD
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-blue-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-green-600", 교대역, 양재역, 5);
=======
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 100);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 200);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 300);
>>>>>>> feat: 노선 별 추가요금 구현

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1550);
    }

    @DisplayName("추가 요금 정책 적용 후 여러 노선을 지날 때 요금을 계산한다.")
    @Test
    void calculateFareWithExtraFarePolicy() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(4L, 1L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(남부터미널역, 양재역, 강남역));
        총_거리가_응답됨(response, 12);
        총_요금이_응답됨(response, 1650);

    }

    @DisplayName("로그인 되어 있는 경우 나이에 맞는 적절한 요금을 계산한다.")
    @Test
    void calculateFareWithAgePolicy() {
        // given
        TokenResponse 어린이_토큰 = 로그인_요청(new TokenRequest(어린이.getEmail(), PASSWORD));
        TokenResponse 청소년_토큰 = 로그인_요청(new TokenRequest(청소년.getEmail(), PASSWORD));
        TokenResponse 성인_토큰 = 로그인_요청(new TokenRequest(성인.getEmail(), PASSWORD));

        // when
        PathResponse 어린이_경로 = 로그인_된_상태로_거리_경로_조회_요청(4L, 1L, 어린이_토큰).as(PathResponse.class);
        PathResponse 청소년_경로 = 로그인_된_상태로_거리_경로_조회_요청(4L, 1L, 청소년_토큰).as(PathResponse.class);
        PathResponse 성인_경로 = 로그인_된_상태로_거리_경로_조회_요청(4L, 1L, 성인_토큰).as(PathResponse.class);

        // then
        assertThat(어린이_경로.getFare()).isEqualTo(1000);
        assertThat(청소년_경로.getFare()).isEqualTo(1390);
        assertThat(성인_경로.getFare()).isEqualTo(1650);
    }

    public static ExtractableResponse<Response> 로그인_된_상태로_거리_경로_조회_요청(long source, long target, TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    public static TokenResponse 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured
            .given().log().all()
            .body(tokenRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/login/token")
            .then().log().all()
            .extract()
            .as(TokenResponse.class);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public void 회원_가입되어_있음(MemberRequest memberRequest) {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/members");
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

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    private static void 총_요금이_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }
}
