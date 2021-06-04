package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음_withToken;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음_withToken;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 고속터미널역;
    private StationResponse 선릉역;
    private StationResponse 옥수역;
    private StationResponse 금호역;
    private TokenResponse 사용자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        강남역 = 지하철역_등록되어_있음_withToken(사용자, "강남역");
        양재역 = 지하철역_등록되어_있음_withToken(사용자, "양재역");
        교대역 = 지하철역_등록되어_있음_withToken(사용자, "교대역");
        남부터미널역 = 지하철역_등록되어_있음_withToken(사용자, "남부터미널역");
        고속터미널역 = 지하철역_등록되어_있음_withToken(사용자, "고속터미널역");
        선릉역 = 지하철역_등록되어_있음_withToken(사용자, "선릉역");
        옥수역 = 지하철역_등록되어_있음_withToken(사용자, "옥수역");
        금호역 = 지하철역_등록되어_있음_withToken(사용자, "금호역");

        신분당선 = 지하철_노선_등록되어_있음_withToken(사용자, new LineRequest("신분당선", "bg-red-600", 900,강남역.getId(), 양재역.getId(), 10));
        이호선 = 지하철_노선_등록되어_있음_withToken(사용자, new LineRequest("이호선", "bg-yellow-600", 교대역.getId(), 강남역.getId(), 10));
        삼호선 = 지하철_노선_등록되어_있음_withToken(사용자, new LineRequest("삼호선", "bg-blue-600", 교대역.getId(), 양재역.getId(), 5));

        지하철_구간_등록되어_있음(사용자, 삼호선, 교대역, 남부터미널역, 3);
        지하철_구간_등록되어_있음(사용자, 신분당선, 고속터미널역, 강남역, 5);
        지하철_구간_등록되어_있음(사용자, 신분당선, 양재역, 선릉역, 5);
        지하철_구간_등록되어_있음(사용자, 이호선, 강남역, 옥수역, 5);
        지하철_구간_등록되어_있음(사용자, 이호선, 옥수역, 금호역, 50);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.(비회원) - 거리별 추가 요금 (10KM이내)")
    @Test
    void findPathByDistanceAndNonMember_10KM() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 양재역.getId());

        // then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1_250);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.(비회원) - 거리별 추가 요금 (10KM ~ 50KM)")
    @Test
    void findPathByDistanceAndNonMember_10KM_50KM() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 옥수역.getId());

        // then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 강남역, 옥수역));
        총_거리가_응답됨(response, 15);
        총_요금이_응답됨(response, 1_350);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.(비회원) - 거리별 추가 요금 (50KM이상)")
    @Test
    void findPathByDistanceAndNonMember_50KM() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(강남역.getId(), 금호역.getId());

        // then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 옥수역, 금호역));
        총_거리가_응답됨(response, 55);
        총_요금이_응답됨(response, 2_150);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.(비회원) - 거리별 추가 요금 (50KM이상) + 노선 추가 요금")
    @Test
    void findPathByDistanceAndLineFareAndNonMember() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(남부터미널역.getId(), 금호역.getId());

        // then
        적절한_경로_응답됨(response, Lists.newArrayList(남부터미널역, 양재역, 강남역, 옥수역, 금호역));
        총_거리가_응답됨(response, 67);
        총_요금이_응답됨(response, 3_250);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.(회원) - 거리별 추가 요금 (50KM이상) + 회원 할인 (어린이)")
    @Test
    void findPathByLineExtraFareWithDistance() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청_withToken(사용자, 강남역.getId(), 금호역.getId());

        // then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 옥수역, 금호역));
        총_거리가_응답됨(response, 55);
        총_요금이_응답됨(response, 900);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.(회원) - 거리별 추가 요금 (50KM이상) + 노선별 추가 요금 + 회원 할인 (어린이)")
    @Test
    void findPathByDistanceAndLineFareAndChild() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청_withToken(사용자, 남부터미널역.getId(), 금호역.getId());

        // then
        적절한_경로_응답됨(response, Lists.newArrayList(남부터미널역, 양재역, 강남역, 옥수역, 금호역));
        총_거리가_응답됨(response, 67);
        총_요금이_응답됨(response, 1_450);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청_withToken(TokenResponse tokenResponse, long source, long target) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
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

    private void 총_요금이_응답됨(ExtractableResponse<Response> response, int totalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDefaultFare()).isEqualTo(totalFare);
    }
}
