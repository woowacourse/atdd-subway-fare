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
public class ShortestPathAcceptanceTest extends AcceptanceTest {
    private StationResponse C역;
    private StationResponse A역;
    private StationResponse B역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        A역 = 지하철역_등록되어_있음("A역");
        B역 = 지하철역_등록되어_있음("B역");
        C역 = 지하철역_등록되어_있음("C역");

        지하철_노선_등록되어_있음("1호선", A역, B역, 5);
        지하철_노선_등록되어_있음("2호선", B역, C역, 5);
        지하철_노선_등록되어_있음("3호선", A역, C역, 11);
    }

    @DisplayName("두 역의 최단 거리 경로와 거리를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), C역.getId());

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(A역, B역, C역));
        총_거리가_응답됨(response, 10);
    }
}
