package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.회원가입_후_로그인;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
    private LineResponse 사호선;
    private LineResponse 오호선;
    private LineResponse 육호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 고속터미널역;
    private StationResponse 신림역;
    private StationResponse 선정릉역;

    private TokenResponse tokenResponse;

    /**
     * 교대역    --- *2호선* ---   강남역 |
     * | *3호선*                   *신분당선* | |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenResponse = 회원가입_후_로그인();
        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        양재역 = 지하철역_등록되어_있음("양재역", tokenResponse);
        교대역 = 지하철역_등록되어_있음("교대역", tokenResponse);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역", tokenResponse);

        고속터미널역 = 지하철역_등록되어_있음("고속터미널역", tokenResponse);
        신림역 = 지하철역_등록되어_있음("신림역", tokenResponse);
        선정릉역 = 지하철역_등록되어_있음("선정릉역", tokenResponse);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-cobalt-blue-600", 강남역, 양재역, 10, tokenResponse);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-cobalt-red-600", 교대역, 강남역, 10, tokenResponse);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-rocky-red-600", 교대역, 양재역, 5, tokenResponse);

        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-cobalt-black-600", 양재역, 고속터미널역, 0,  2, tokenResponse);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-choonsik-600", 신림역, 고속터미널역, 500,  2, tokenResponse);
        육호선 = 지하철_노선_등록되어_있음("육호선", "bg-starbucks-600", 신림역, 선정릉역, 700,  1, tokenResponse);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3, tokenResponse);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 게스트")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, 1_250);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 멤버, 추가요금 x")
    @ParameterizedTest
    @CsvSource(value = {"20:1250", "15:1070", "8:800", "5:0"}, delimiter = ':')
    void findPathByDistanceForLoginMember(int age, int fare) {
        //given
        TokenResponse adultTokenResponse = 회원가입_후_로그인("another@email.com", age);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, adultTokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, fare);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 멤버, 추가요금 o")
    @ParameterizedTest
    @CsvSource(value = {"20:1950", "15:1630", "8:1150", "5:0"}, delimiter = ':')
    void findPathByDistanceForLoginMemberWithExtraFare(int age, int fare) {
        //given
        TokenResponse adultTokenResponse = 회원가입_후_로그인("another@email.com", age);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(2L, 7L, adultTokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(양재역, 고속터미널역, 신림역, 선정릉역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, fare);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target,
            TokenResponse adultTokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(adultTokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 적절한_경로_응답됨(ExtractableResponse<Response> response, List<StationResponse> expectedPath) {
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

    public static void 총_요금이_응답됨(ExtractableResponse<Response> response, int totalPrice) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPrice()).isEqualTo(totalPrice);
    }
}
