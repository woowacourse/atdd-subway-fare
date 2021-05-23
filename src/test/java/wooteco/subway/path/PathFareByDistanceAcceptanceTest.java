package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.station.dto.StationResponse;

import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.path.PathTest.*;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회 - 거리에 따른 추가요금")
public class PathFareByDistanceAcceptanceTest extends AcceptanceTest {
    private StationResponse A역;
    private StationResponse B역;
    private StationResponse C역;
    private StationResponse D역;
    private StationResponse E역;
    private StationResponse F역;
    private StationResponse G역;
    private StationResponse H역;
    private StationResponse I역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        A역 = 지하철역_등록되어_있음("A역");
        B역 = 지하철역_등록되어_있음("B역");
        C역 = 지하철역_등록되어_있음("C역");
        D역 = 지하철역_등록되어_있음("D역");
        E역 = 지하철역_등록되어_있음("E역");
        F역 = 지하철역_등록되어_있음("F역");
        G역 = 지하철역_등록되어_있음("G역");
        H역 = 지하철역_등록되어_있음("H역");
        I역 = 지하철역_등록되어_있음("I역");

        지하철_노선_등록되어_있음("1호선", A역, B역, 10);
        지하철_노선_등록되어_있음("2호선", A역, C역, 11);
        지하철_노선_등록되어_있음("3호선", A역, D역, 15);
        지하철_노선_등록되어_있음("4호선", A역, E역, 16);
        지하철_노선_등록되어_있음("5호선", A역, F역, 50);
        지하철_노선_등록되어_있음("6호선", A역, G역, 51);
        지하철_노선_등록되어_있음("7호선", A역, H역, 58);
        지하철_노선_등록되어_있음("8호선", A역, I역, 59);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 10km")
    @Test
    void findFareByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), B역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, B역));
        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 11km")
    @Test
    void findFareByDistance2() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), C역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, C역));
        총_거리가_응답됨(response, 11);
        총_요금이_응답됨(response, 1350);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 15km")
    @Test
    void findFareByDistance3() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), D역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, D역));
        총_거리가_응답됨(response, 15);
        총_요금이_응답됨(response, 1350);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 16km")
    @Test
    void findFareByDistance4() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), E역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, E역));
        총_거리가_응답됨(response, 16);
        총_요금이_응답됨(response, 1450);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 50km")
    @Test
    void findFareByDistance5() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), F역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, F역));
        총_거리가_응답됨(response, 50);
        총_요금이_응답됨(response, 2050);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 51km")
    @Test
    void findFareByDistance6() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), G역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, G역));
        총_거리가_응답됨(response, 51);
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 58km")
    @Test
    void findFareByDistance7() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), H역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, H역));
        총_거리가_응답됨(response, 58);
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("두 역 사이의 요금을 조회한다. - 59km")
    @Test
    void findFareByDistance8() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), I역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, I역));
        총_거리가_응답됨(response, 59);
        총_요금이_응답됨(response, 2250);
    }
}
