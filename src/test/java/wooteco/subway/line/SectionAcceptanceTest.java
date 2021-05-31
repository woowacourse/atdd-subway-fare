package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionDistanceRequest;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        정자역 = createStation("정자역");
        광교역 = createStation("광교역");

        신분당선 = createLine(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10));
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = addSection(신분당선.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 3));

        // then
        assertSection(response, 신분당선, Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when

        addSection(신분당선.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 2));
        ExtractableResponse<Response> response = addSection(신분당선.getId(), new SectionRequest(정자역.getId(), 강남역.getId(), 5));

        // then
        assertSection(response, 신분당선, Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = addSection(신분당선.getId(), new SectionRequest(강남역.getId(), 광교역.getId(), 3));

        // then
        assertBadRequest(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = addSection(신분당선.getId(), new SectionRequest(정자역.getId(), 양재역.getId(), 3));

        // then
        assertBadRequest(response);
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        addSection(신분당선.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 2));
        addSection(신분당선.getId(), new SectionRequest(양재역.getId(), 정자역.getId(), 2));

        // when
        ExtractableResponse<Response> removeResponse = deleteSection(신분당선, 양재역);

        // then
        assertSection(removeResponse, 신분당선, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = deleteSection(신분당선, 강남역);

        // then
        assertBadRequest(removeResponse);
    }

    @DisplayName("지하철 구간 거리를 수정한다")
    @Test
    void updateDistance() {
        SectionDistanceRequest sectionDistanceRequest = new SectionDistanceRequest(20);

        updateSectionDistance(신분당선.getId(), 강남역, 광교역, sectionDistanceRequest);

        ExtractableResponse<Response> pathResponse = findPath(강남역.getId(), 광교역.getId(), new TokenResponse(""));
        assertDistance(pathResponse, sectionDistanceRequest.getDistance());
    }

    private ExtractableResponse<Response> updateSectionDistance(
            final Long id,
            final StationResponse upStationResponse,
            final StationResponse downStationResponse,
            final SectionDistanceRequest sectionDistanceRequest) {

        return RestAssured
                .given().log().all()
                .queryParam("upStationId", upStationResponse.getId())
                .queryParam("downStationId", downStationResponse.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionDistanceRequest)
                .when().put("/lines/{lineId}/sections", id)
                .then().log().all()
                .extract();
    }

    private void assertStationsSorted(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private ExtractableResponse<Response> deleteSection(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    private void assertSection(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertOk(result);
        ExtractableResponse<Response> response = getLine(lineResponse);
        assertStationsSorted(response, stationResponses);
    }
}
