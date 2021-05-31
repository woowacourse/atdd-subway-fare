package wooteco.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.dto.StationLineResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = createStationRequest(강남역);

        // then
        assertCreated(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        createStation(강남역);

        // when
        ExtractableResponse<Response> response = createStationRequest(강남역);

        // then
        assertBadRequest(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        StationResponse stationResponse1 = createStation(강남역);
        StationResponse stationResponse2 = createStation(역삼역);
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 100, stationResponse1.getId(), stationResponse2.getId(), 10);
        LineRequest lineRequest2 = new LineRequest("구신분당선", "bg-red-600", stationResponse1.getId(), stationResponse2.getId(), 15);
        createLineRequest(lineRequest1);
        createLineRequest(lineRequest2);

        StationLineResponse stationLineResponse1 = new StationLineResponse(stationResponse1.getId(), stationResponse1.getName(),
                Arrays.asList(new TransferLineResponse(1L, lineRequest1.getName(), lineRequest1.getColor()),
                        new TransferLineResponse(2L, lineRequest2.getName(), lineRequest2.getColor())));
        StationLineResponse stationLineResponse2 = new StationLineResponse(stationResponse2.getId(), stationResponse2.getName(),
                Arrays.asList(new TransferLineResponse(1L, lineRequest1.getName(), lineRequest1.getColor()),
                        new TransferLineResponse(2L, lineRequest2.getName(), lineRequest2.getColor())));

        // when
        ExtractableResponse<Response> response = getAllStations();

        // then
        assertOk(response);
        assertStationsWithLines(response, Arrays.asList(stationLineResponse1, stationLineResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = createStation(강남역);

        // when
        ExtractableResponse<Response> response = deleteStation(stationResponse);

        // then
        assertNoContent(response);
    }

    private ExtractableResponse<Response> getAllStations() {
        return RestAssured
                .given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private void assertStationsWithLines(ExtractableResponse<Response> response, List<StationLineResponse> expectedResponse) {
        assertThat(Arrays.asList(response.as(StationLineResponse[].class)))
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    private ExtractableResponse<Response> deleteStation(StationResponse stationResponse) {
        return RestAssured
                .given().log().all()
                .when().delete("/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }
}
