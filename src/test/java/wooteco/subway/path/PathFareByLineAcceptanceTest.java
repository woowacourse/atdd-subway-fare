package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
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
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회 - 노선에 따른 추가요금")
public class PathFareByLineAcceptanceTest extends AcceptanceTest {
    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;

    private StationResponse a역;
    private StationResponse b역;
    private StationResponse c역;
    private StationResponse d역;
    private StationResponse e역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        a역 = 지하철역_등록되어_있음("a역");
        b역 = 지하철역_등록되어_있음("b역");
        c역 = 지하철역_등록되어_있음("c역");
        d역 = 지하철역_등록되어_있음("d역");
        e역 = 지하철역_등록되어_있음("e역");

        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-red-600", 0, a역, b역, 1);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 500, b역, c역, 1);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 900, c역, d역, 1);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-600", 900, b역, e역, 1);
    }

    @DisplayName("추가요금 0원인 노선 이용.")
    @Test
    void findFareByLine1() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, b역));
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("추가요금 500원인 노선 이용.")
    @Test
    void findFareByLine2() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(2L, 3L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(b역, c역));
        총_요금이_응답됨(response, 1750);
    }

    @DisplayName("추가요금 900원인 노선 이용.")
    @Test
    void findFareByLine3() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(3L, 4L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(c역, d역));
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("추가요금 0원, 500원인 노선 이용")
    @Test
    void findFareByLine4() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 3L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, b역, c역));
        총_요금이_응답됨(response, 1750);
    }

    @DisplayName("추가요금 0원, 900원인 노선 이용")
    @Test
    void findFareByLine5() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 5L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, b역, e역));
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("추가요금 500원, 900원인 노선 이용")
    @Test
    void findFareByLine6() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(2L, 4L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(b역, c역, d역));
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("추가요금 0원, 500원, 900원인 노선 이용")
    @Test
    void findFareByLine7() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 4L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(a역, b역, c역, d역));
        총_요금이_응답됨(response, 2150);
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

    public static void 총_요금이_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }
}
