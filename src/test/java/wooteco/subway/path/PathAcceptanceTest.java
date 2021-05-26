package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    private LineResponse 오호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 잠실역;
    private StationResponse 석촌역;

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
        잠실역 = 지하철역_등록되어_있음("잠실역");
        석촌역 = 지하철역_등록되어_있음("석촌역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-red-600", 잠실역, 석촌역, 10);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역.getId(), 양재역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 5);
    }

    @DisplayName("도달할 수 없는 최단 경로는 예외 처리한다.")
    @Test
    void invalidPath() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 6L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat((String) response.body().jsonPath().get("message")).isEqualTo("해당되는 최단 경로를 찾을 수 없습니다.");
    }

    @DisplayName("경로 요금 조회 - 10km 초과 50km 이하 : 5km 마다 100 / 50km 초과 시 : 8km마다 100원")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "50:2050", "51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void findFare(int distance, int fare) {
        StationResponse 노량진역 = 지하철역_등록되어_있음("노량진");
        StationResponse 여의도역 = 지하철역_등록되어_있음("여의도역");

        LineResponse 육호선 = 지하철_노선_등록되어_있음("육호선", "bg-red-600", 노량진역, 여의도역, distance);

        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(노량진역.getId(), 여의도역.getId());

        //ten
        적절한_요금_응답됨(response, fare);
    }

    private void 적절한_요금_응답됨(ExtractableResponse<Response> response, int fare) {
        assertThat(response.as(PathResponse.class).getExtraFare()).isEqualTo(fare);
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
        assertThat(pathResponse.getExtraFare()).isEqualTo(1250);
    }
}
