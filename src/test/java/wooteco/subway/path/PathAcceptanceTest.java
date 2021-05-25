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
    private LineResponse 구호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private StationResponse 가양역;
    private StationResponse 등촌역;
    private StationResponse 염창역;
    private StationResponse 당산역;
    private StationResponse 여의도역;

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

        가양역 = 지하철역_등록되어_있음("가양역");
        등촌역 = 지하철역_등록되어_있음("등촌역");
        염창역 = 지하철역_등록되어_있음("염창역");
        당산역 = 지하철역_등록되어_있음("당산역");
        여의도역 = 지하철역_등록되어_있음("여의도역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 20);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 10);

        구호선 = 지하철_노선_등록되어_있음("구호선", "황금색", 가양역, 등촌역, 9);
        지하철_구간_등록되어_있음(구호선, 등촌역, 염창역, 40);
        지하철_구간_등록되어_있음(구호선, 염창역, 당산역, 1);
        지하철_구간_등록되어_있음(구호선, 당산역, 여의도역, 1);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다 - 10km 미만")
    @Test
    void findPatAndFareWhenDistanceUnderNINE() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(5L, 6L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역));
        총_거리가_응답됨(response, 9);
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다. - 10km")
    @Test
    void findPatAndFareWhenDistanceUnderTEN() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다 - 10km 초과 50km 미만")
    @Test
    void findPatAndFareWhenDistanceUnderBetweenTENtoFIFTY() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(5L, 7L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역));
        총_거리가_응답됨(response, 49);
        총_요금이_응답됨(response, 2050);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다 - 50km")
    @Test
    void findPatAndFareWhenDistanceIsFIFTY() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(5L, 8L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역));
        총_거리가_응답됨(response, 50);
        총_요금이_응답됨(response, 2050);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다 - 50km 초과")
    @Test
    void findPatAndFareWhenDistanceIsOverFIFTY() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(5L, 9L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역, 여의도역));
        총_거리가_응답됨(response, 51);
        총_요금이_응답됨(response, 2150);
    }

    private void 총_요금이_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDefaultFare()).isEqualTo(fare);
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
