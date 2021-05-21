package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationResponse;

import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.path.PathTest.*;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회 - 노선에 따른 추가요금")
public class PathFareByLineAcceptanceTest extends AcceptanceTest {
    private static final String COLOR = "bg-red-600";

    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse A역;
    private StationResponse B역;
    private StationResponse C역;
    private StationResponse D역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        A역 = 지하철역_등록되어_있음("A역");
        B역 = 지하철역_등록되어_있음("B역");
        C역 = 지하철역_등록되어_있음("C역");
        D역 = 지하철역_등록되어_있음("D역");

        일호선 = 지하철_노선_등록되어_있음("1호선", A역, B역, 1);
        이호선 = 지하철_노선_등록되어_있음("2호선", B역, C역, 1);
        삼호선 = 지하철_노선_등록되어_있음("3호선", C역, D역, 1);
    }

    @DisplayName("추가요금 0원인 노선 이용.")
    @Test
    void findFareByLine1() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), B역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, B역));
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("추가요금 500원인 노선 이용.")
    @Test
    void findFareByLine2() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(B역.getId(), C역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(B역, C역));
        총_요금이_응답됨(response, 1750);
    }

    @DisplayName("추가요금 900원인 노선 이용.")
    @Test
    void findFareByLine3() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(C역.getId(), D역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(C역, D역));
        총_요금이_응답됨(response, 2150);
    }
}
