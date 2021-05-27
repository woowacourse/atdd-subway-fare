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
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String loginToken;

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target, String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
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

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    public static void 요금_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

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
        회원_등록되어_있음("kevin@naver.com", "123", 7);
        loginToken = 로그인되어_있음("kevin@naver.com", "123").getAccessToken();

        강남역 = 지하철역_등록되어_있음("강남역", loginToken);
        양재역 = 지하철역_등록되어_있음("양재역", loginToken);
        교대역 = 지하철역_등록되어_있음("교대역", loginToken);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역", loginToken);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, loginToken);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-601", 교대역, 강남역, 10, loginToken);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-602", 교대역, 양재역, 5, loginToken);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3, loginToken);
    }

    @DisplayName("비회원으로 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", 3L, 2L)
                .then().log().all()
                .extract();

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        요금_응답됨(response, 1250);
    }

    @DisplayName("아동 나이 회원으로 경로 조회를 요청한다.")
    @Test
    void findPathWhenChildren() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, loginToken);

        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        요금_응답됨(response, 450);
    }

    @DisplayName("청소년 나이 회원으로 경로 조회를 요청한다.")
    @Test
    void findPathWhenTeenager() {
        회원_등록되어_있음("abc@naver.com", "pass", 16);
        TokenResponse token = 로그인되어_있음("abc@naver.com", "pass");
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, token.getAccessToken());

        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        요금_응답됨(response, 720);
    }
}
