package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 다른노선위의역1;
    private StationResponse 다른노선위의역2;

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
        다른노선위의역1 = 지하철역_등록되어_있음("다른노선위의역1");
        다른노선위의역2 = 지하철역_등록되어_있음("다른노선위의역2");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 0);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-yellow-600", 교대역, 강남역, 10, 500);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-black-600", 교대역, 양재역, 5, 900);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-green-600", 다른노선위의역2, 다른노선위의역1, 5, 900);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 양재역.getId());

        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        int expectedExtraFare = 삼호선.getExtraFare();
        총_거리가_응답됨(response, 5, 1250 + expectedExtraFare);
    }

    @DisplayName("도달 할 수 없는 두 역의 최단 거리 경로를 조회한다. :: 역은 존재하지만 경로가 없음")
    @Test
    void findNonExistPath1() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(다른노선위의역1.getId(), 강남역.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("도달 할 수 없는 두 역의 최단 거리 경로를 조회한다. :: 존재하지 않는 역 id")
    @Test
    void findNonExistPath2() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(Long.MAX_VALUE, 강남역.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("도달 할 수 없는 두 역의 최단 거리 경로를 조회한다. :: 음의 역 id")
    @Test
    void findNonExistPath3() {
        ExtractableResponse<Response> response = 거리_경로_조회_요청(-1, 강남역.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance, int totalFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
        assertThat(pathResponse.getFare()).isEqualTo(totalFare);
    }
}
