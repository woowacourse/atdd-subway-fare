package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.CustomLineResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("추가 지하철 노선 관련 기능")
public class CustomLineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 상봉역;
    private StationResponse 면목역;

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        광교역 = 지하철역_등록되어_있음("광교역");
        상봉역 = 지하철역_등록되어_있음("상봉역");
        면목역 = 지하철역_등록되어_있음("면목역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("7호선", "bg-green-600", 상봉역.getId(), 면목역.getId(), 15);
    }

    @DisplayName("지하철 노선 목록을 상,하행종점역과 총거리와 함께 조회한다.")
    @Test
    void findLinesWithStationsAndTotalDistance() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
        지하철_노선_목록_상하행종점역_일치함(response, Arrays.asList(lineResponse1, lineResponse2));
        지하철_노선_총_거리_일치함(response, Arrays.asList(lineRequest1, lineRequest2));
    }

    private void 지하철_노선_총_거리_일치함(ExtractableResponse<Response> response, List<LineRequest> lineRequests) {
        List<CustomLineResponse> customLineResponses = response.jsonPath().getList(".", CustomLineResponse.class);
        for (int i = 0; i < lineRequests.size(); i++) {
            CustomLineResponse customLineResponse = customLineResponses.get(i);
            LineRequest lineRequest = lineRequests.get(i);

            assertThat(customLineResponse.getDistance()).isEqualTo(lineRequest.getDistance());
        }
    }

    private void 지하철_노선_목록_상하행종점역_일치함(ExtractableResponse<Response> response, List<LineResponse> lineResponses) {
        List<CustomLineResponse> customLineResponses = response.jsonPath().getList(".", CustomLineResponse.class);
        for (int i = 0; i < lineResponses.size(); i++) {
            CustomLineResponse customLineResponse = customLineResponses.get(i);
            LineResponse lineResponse = lineResponses.get(i);
            int size = lineResponse.getStations().size();

            지하철_노선_상하행종점역_일치함(customLineResponse, lineResponse, size);
        }
    }

    private void 지하철_노선_상하행종점역_일치함(CustomLineResponse customLineResponse, LineResponse lineResponse, int size) {
        assertThat(customLineResponse.getStartStation())
            .usingRecursiveComparison()
            .isEqualTo(lineResponse.getStations().get(0));

        assertThat(customLineResponse.getEndStation())
            .usingRecursiveComparison()
            .isEqualTo(lineResponse.getStations().get(size - 1));
    }


    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/api/lines")
            .then().log().all().
                extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/lines")
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", CustomLineResponse.class).stream()
            .map(CustomLineResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
