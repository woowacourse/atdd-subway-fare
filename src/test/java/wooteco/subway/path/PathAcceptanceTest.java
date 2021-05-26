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
import wooteco.subway.line.LineAcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음_외부토큰;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음_외부토큰;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 정자역;

    private static TokenResponse tokenResponse;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     * |                        |
     * |                        정자역
     * |                        |
     * |----------------------
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        //회원 가입 및 로그인
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        강남역 = 지하철역_등록되어_있음_외부토큰(tokenResponse, "강남역");
        양재역 = 지하철역_등록되어_있음_외부토큰(tokenResponse, "양재역");
        교대역 = 지하철역_등록되어_있음_외부토큰(tokenResponse, "교대역");
        남부터미널역 = 지하철역_등록되어_있음_외부토큰(tokenResponse, "남부터미널역");
        정자역 = 지하철역_등록되어_있음_외부토큰(tokenResponse, "정자역");

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음_외부토큰(tokenResponse, "신분당선", "bg-red-600", 강남역, 정자역, 300, 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음_외부토큰(tokenResponse, "이호선", "bg-blue-600", 교대역, 강남역, 500, 10);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음_외부토큰(tokenResponse, "삼호선", "bg-yellow-600", 교대역, 양재역, 0, 5);

        지하철_구간_등록되어_있음_외부토큰(tokenResponse, 삼호선, 교대역, 남부터미널역, 3);
        지하철_구간_등록되어_있음_외부토큰(tokenResponse, 신분당선, 강남역, 양재역, 8);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다 - 3호선 경유")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        적절한_요금_응답됨(response, 1250);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다 - 신분당선 경유")
    @Test
    void findPathByDistanceWithExtraFare() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 5L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역, 정자역));
        총_거리가_응답됨(response, 7);
        적절한_요금_응답됨(response, 1550);
    }

    private void 적절한_요금_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
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

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }
}
