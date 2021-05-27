package wooteco.subway.path;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.auth.AuthAcceptanceTest.*;
import static wooteco.subway.line.LineAcceptanceTest.*;
import static wooteco.subway.line.SectionAcceptanceTest.*;
import static wooteco.subway.station.StationAcceptanceTest.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;

import com.google.common.collect.Lists;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    public static ExtractableResponse<Response> 로그인_없이_거리_경로_조회_요청(long source, long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/paths?source={sourceId}&target={targetId}", source, target)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(TokenResponse tokenResponse, long source, long target) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
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

    public static void 적절한_요금이_응답됨(ExtractableResponse<Response> response, int totalPrice) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPrice()).isEqualTo(totalPrice);
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

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 100);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 1000);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 10000);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("로그인 하지 않고 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 로그인_없이_거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        적절한_요금이_응답됨(response, 11250);
    }

    @DisplayName("로그인과 함께 두 역의 최단 거리 경로를 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {"3:0", "6:5450", "13:8720", "20:11250"}, delimiter = ':')
    void findPathByDistanceWithMember(int age, int price) {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, age);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(tokenResponse, 3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
        적절한_요금이_응답됨(response, price);
    }
}
