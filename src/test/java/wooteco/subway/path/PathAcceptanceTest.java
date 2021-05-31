package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
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
import wooteco.subway.auth.AuthAcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.member.MemberAcceptanceTest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 신림역;
    private StationResponse 봉천역;
    private StationResponse 서울대역;
    private TokenResponse tokenResponse;

    /**
     * 교대역    --- *2호선* ---   강남역 |                        | *3호선*                   *신분당선* |
     * | 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenResponse = AuthAcceptanceTest.회원가입_토큰가져오기();

        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        양재역 = 지하철역_등록되어_있음("양재역", tokenResponse);
        교대역 = 지하철역_등록되어_있음("교대역", tokenResponse);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역", tokenResponse);
        신림역 = 지하철역_등록되어_있음("신림역", tokenResponse);
        봉천역 = 지하철역_등록되어_있음("봉천역", tokenResponse);
        서울대역 = 지하철역_등록되어_있음("서울대역", tokenResponse);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 100, tokenResponse);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 교대역, 강남역, 10, 200, tokenResponse);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-yellow-600", 교대역, 양재역, 5, 300, tokenResponse);

        사호선 = 지하철_노선_등록되어_있음("사호선", "blue", 신림역, 봉천역, 40, 0, tokenResponse);
        오호선 = 지하철_노선_등록되어_있음("오호선", "white", 봉천역, 서울대역, 19, 0, tokenResponse);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3, tokenResponse);
        지하철_구간_등록되어_있음(사호선, 신림역, 강남역, 1, tokenResponse);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L, tokenResponse);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        총_가격이_응답됨(response, 1250 + 300);
    }

    @DisplayName("최단거리가 40km 일 때 나이에 따른 비용 테스트")
    @ParameterizedTest
    @CsvSource(value = {"20:1850", "15:1550", "8:1100"}, delimiter = ':')
    void findPathByDistanceByForty(String age, String fare) {
        TokenResponse token = getTokenResponse(Integer.parseInt(age));

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(5L, 6L, token);

        //then
        총_거리가_응답됨(response, 40);
        총_가격이_응답됨(response, Integer.parseInt(fare));
    }

    @DisplayName("최단거리가 59km 일때 나이에 따른 비용 테스트")
    @ParameterizedTest
    @CsvSource(value = {"20:2250", "15:1870", "8:1300"}, delimiter = ':')
    void findPathByDistanceByOverFifty(String age, String fare) {
        // given
        TokenResponse token = getTokenResponse(Integer.parseInt(age));

        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(5L, 7L, token);

        총_거리가_응답됨(response, 59);
        총_가격이_응답됨(response, Integer.parseInt(fare));
    }

    @DisplayName("여러 노선을 이용할 때 제일 높은 노선의 요금을 추가하는데 나이에 따른 비용 테스트")
    @ParameterizedTest
    @CsvSource(value = {"20:1550", "15:1310", "8:950"}, delimiter = ':')
    void expensiveExtraFareLine(String age, String fare) {
        // given
        TokenResponse token = getTokenResponse(Integer.parseInt(age));

        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(5L, 3L, token);

        // then
        총_거리가_응답됨(response, 11);
        총_가격이_응답됨(response, Integer.parseInt(fare));
    }

    private ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target,
        TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    private void 적절한_경로_응답됨(ExtractableResponse<Response> response,
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

    private void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    private void 총_가격이_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    private TokenResponse getTokenResponse(int age) {
        MemberAcceptanceTest.회원_생성을_요청(
            MemberAcceptanceTest.NEW_EMAIL,
            MemberAcceptanceTest.PASSWORD,
            age);
        return AuthAcceptanceTest
            .로그인되어_있음(MemberAcceptanceTest.NEW_EMAIL, MemberAcceptanceTest.PASSWORD);
    }
}
