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

// TODO 최단거리 테스트는 파일 분리하기
@DisplayName("지하철 경로 조회 - 거리에 따른 추가요금")
public class PathFareByDistanceAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private LineResponse 사호선;
    private LineResponse 오호선;
    private LineResponse 육호선;
    private LineResponse 칠호선;
    private LineResponse 팔호선;
    private LineResponse 구호선;
    private LineResponse 십호선;
    private LineResponse 십일호선;

    private StationResponse x역;
    private StationResponse c역;
    private StationResponse a역;
    private StationResponse b역;
    private StationResponse d역;
    private StationResponse e역;
    private StationResponse f역;
    private StationResponse g역;
    private StationResponse h역;
    private StationResponse i역;
    private StationResponse j역;
    private StationResponse k역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        a역 = 지하철역_등록되어_있음("a역");
        b역 = 지하철역_등록되어_있음("b역");
        c역 = 지하철역_등록되어_있음("c역");
        d역 = 지하철역_등록되어_있음("d역");
        e역 = 지하철역_등록되어_있음("e역");
        f역 = 지하철역_등록되어_있음("f역");
        g역 = 지하철역_등록되어_있음("g역");
        h역 = 지하철역_등록되어_있음("h역");
        i역 = 지하철역_등록되어_있음("i역");
        j역 = 지하철역_등록되어_있음("j역");
        k역 = 지하철역_등록되어_있음("k역");
        x역 = 지하철역_등록되어_있음("x역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 0, x역, c역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 0, a역, x역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 0, a역, c역, 5);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-600", 0, a역, d역, 10);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-red-600", 0, a역, e역, 11);
        육호선 = 지하철_노선_등록되어_있음("육호선", "bg-red-600", 0, a역, f역, 15);
        칠호선 = 지하철_노선_등록되어_있음("칠호선", "bg-red-600", 0, a역, g역, 16);
        팔호선 = 지하철_노선_등록되어_있음("팔호선", "bg-red-600", 0, a역, h역, 50);
        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-red-600", 0, a역, i역, 51);
        십호선 = 지하철_노선_등록되어_있음("십호선", "bg-red-600", 0, a역, j역, 58);
        십일호선 = 지하철_노선_등록되어_있음("십일호선", "bg-red-600", 0, a역, k역, 59);


        지하철_구간_등록되어_있음(삼호선, a역, b역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로와 거리를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 3L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, b역, c역));
        총_거리가_응답됨(response, 5);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 10km")
    @Test
    void findFareByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 4L);

        //then
//        적절한_경로_응답됨(response, Lists.newArrayList(a역, d역));
//        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 11km")
    @Test
    void findFareByDistance2() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 5L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, e역));
        총_거리가_응답됨(response, 11);
        총_요금이_응답됨(response, 1350);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 15km")
    @Test
    void findFareByDistance3() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 6L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, f역));
        총_거리가_응답됨(response, 15);
        총_요금이_응답됨(response, 1350);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 16km")
    @Test
    void findFareByDistance4() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 7L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, g역));
        총_거리가_응답됨(response, 16);
        총_요금이_응답됨(response, 1450);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 50km")
    @Test
    void findFareByDistance5() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 8L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, h역));
        총_거리가_응답됨(response, 50);
        총_요금이_응답됨(response, 2050);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 51km")
    @Test
    void findFareByDistance6() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 9L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, i역));
        총_거리가_응답됨(response, 51);
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 58km")
    @Test
    void findFareByDistance7() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 10L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, j역));
        총_거리가_응답됨(response, 58);
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 59km")
    @Test
    void findFareByDistance8() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 11L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, k역));
        총_거리가_응답됨(response, 59);
        총_요금이_응답됨(response, 2250);
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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

    public static void 총_요금이_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }
}
