package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.회원가입_후_로그인;
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

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-cobalt-blue-600", 강남역, 양재역, 10, tokenResponse);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-cobalt-red-600", 교대역, 강남역, 10, tokenResponse);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-rocky-red-600", 교대역, 양재역, 5, tokenResponse);

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
        총_요금이_응답됨(response, BigDecimal.valueOf(1_250));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. - 멤버")
    @ParameterizedTest
    @CsvSource(value = {"20:1250", "15:720", "8:450", "5:0"}, delimiter = ':')
    void findPathByDistanceForLoginMember(int age, int fare) {
        //given
        TokenResponse adultTokenResponse = 회원가입_후_로그인("another@email.com", age);
        // 청소년(age: 13 ~ 19): 운임에서 350원을 공제한 금액의 20%할인
        // (1250 - 350) * 0.8
        // 어린이(age: 6 ~ 12): 운임에서 350원을 공제한 금액의 50%할인
        // (1250 - 350) * 0.5
        // 베이비(age: 0 ~ 5): 무료

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, adultTokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_요금이_응답됨(response, BigDecimal.valueOf(fare));
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

    public static void 적절한_경로_응답됨(ExtractableResponse<Response> response,
            ArrayList<StationResponse> expectedPath) {
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

    public static void 총_요금이_응답됨(ExtractableResponse<Response> response, BigDecimal totalPrice) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPrice()).isEqualTo(totalPrice);
    }
}
