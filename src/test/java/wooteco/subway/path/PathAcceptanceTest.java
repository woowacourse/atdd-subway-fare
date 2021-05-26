package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
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
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-601", 강남역, 양재역, 10, 100);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-602", 교대역, 강남역, 10, 200);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-603", 교대역, 양재역, 5, 300);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로와 노선 추가요금에 따른 총 요금을 조회한다.")
    @Test
    void findPathByDistanceWithLineExtraFare() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1250 + 300);
    }

    @DisplayName("두 역의 최단 거리 경로와 노선 거리 추가요금에 따른 총 요금을 조회한다.")
    @Test
    void findPathByDistanceWithLineExtraFareByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 4L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 남부터미널역));
        총_거리가_응답됨(response, 12);
        총_요금이_응답됨(response, 1250 + 100 + 300);
    }

    @DisplayName("로그인 상태에서 경로 조회 시, 로그인 되어있는 사용자의 나이에 따라 요금 할인을 적용한다.")
    @ParameterizedTest(name = "{3}")
    @CsvSource({
        "adult@email.com, 20, 1650, 성인 요금",
        "teenager@email.com, 15, 1040, 청소년 요금",
        "child@email.com, 12, 650, 아동 요금"
    })
    void findPathByDistanceWithLineExtraFareByDistanceAsLoginMember(String email, int age, int expectedFare, String testCaseName) {
        //when
        회원_등록되어_있음(email, "PASSWORD", age);
        TokenResponse tokenResponse = 로그인되어_있음(email, "PASSWORD");
        ExtractableResponse<Response> response = 거리_경로_조회_요청_로그인(1L, 4L, tokenResponse.getAccessToken());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(강남역, 양재역, 남부터미널역));
        총_거리가_응답됨(response, 12);
        총_요금이_응답됨(response, expectedFare);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
            .given(spec).log().all()
            .filter(document("find-path", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청_로그인(long source, long target, String accessToken) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
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

    private void 총_요금이_응답됨(ExtractableResponse<Response> response, int expectedTotalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getTotalFare()).isEqualTo(expectedTotalFare);
    }
}
