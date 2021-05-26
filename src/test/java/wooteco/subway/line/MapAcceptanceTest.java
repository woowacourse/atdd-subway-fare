package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_생성_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineMapResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.dto.StationMapResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 노선도를 불러온다.")
public class MapAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 광교역;
    private LineResponse 신분당선;
    private LineResponse 경의중앙선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        역삼역 = 지하철역_등록되어_있음("역삼역");
        광교역 = 지하철역_등록되어_있음("광교역");

        신분당선 = 지하철_노선_생성_요청(
            new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 900, 10))
            .as(LineResponse.class);
        경의중앙선 = 지하철_노선_생성_요청(new LineRequest("경의중앙선", "bg-red-600", 강남역.getId(), 역삼역.getId(), 20))
            .as(LineResponse.class);
    }


    @DisplayName("지하철 노선도를 생성한다.")
    @Test
    void findMap() {
        ExtractableResponse<Response> response = 노선도_정보_조회();
        List<StationMapResponse> lineStations1 = Arrays
            .asList(new StationMapResponse(강남역.getId(), 강남역.getName(), 10, Arrays.asList(
                TransferLineResponse.of(경의중앙선.getId(), 경의중앙선.getName(), 경의중앙선.getColor()))),
                new StationMapResponse(광교역.getId(), 광교역.getName(), -1, new ArrayList<>()));
        List<StationMapResponse> lineStations2 = Arrays
            .asList(new StationMapResponse(강남역.getId(), 강남역.getName(), 20, Arrays.asList(
                TransferLineResponse.of(신분당선.getId(), 신분당선.getName(), 신분당선.getColor()))),
                new StationMapResponse(역삼역.getId(), 역삼역.getName(), -1, new ArrayList<>()));

        List<LineMapResponse> expected = Arrays.asList(
            LineMapResponse.of(신분당선.getId(), 신분당선.getName(), 신분당선.getColor(), lineStations1),
            LineMapResponse.of(경의중앙선.getId(), 경의중앙선.getName(), 경의중앙선.getColor(), lineStations2));

        assertThat(Arrays.asList(response.as(LineMapResponse[].class)))
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    private ExtractableResponse<Response> 노선도_정보_조회() {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/map")
            .then().log().all()
            .extract();
    }

}
