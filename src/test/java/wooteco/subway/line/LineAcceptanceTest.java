package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.h2.value.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.*;
import wooteco.subway.path.domain.Distance;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationMapResponse;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationTransferResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.domain.Sections.DISTANCE_FOR_DOWN_END_STATION;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 몽촌토성역;
    private StationResponse downStation;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private LineRequest lineRequest3;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = createStation("강남역");
        역삼역 = createStation("역삼역");
        몽촌토성역 = createStation("몽촌토성역");
        downStation = createStation("광교역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 100, 강남역.getId(), downStation.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), downStation.getId(), 15);
        lineRequest3 = new LineRequest("2호선", "bg-red-600", 강남역.getId(), 역삼역.getId(), 20);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = createLineRequest(lineRequest1);

        // then
        assertCreated(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        createLine(lineRequest1);

        // when
        ExtractableResponse<Response> response = createLineRequest(lineRequest1);

        // then
        assertBadRequest(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = createLine(lineRequest1);
        LineResponse lineResponse2 = createLine(lineRequest2);

        // when
        ExtractableResponse<Response> response = getAllLines();

        // then
        assertOk(response);
        assertLines(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse lineResponse = createLine(lineRequest2);

        // when
        ExtractableResponse<Response> response = getLine(lineResponse);

        // then
        assertLine(response, lineResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        // when
        ExtractableResponse<Response> response = updateLine(lineResponse.getId(), lineRequest2);

        // then
        assertOk(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        // when
        ExtractableResponse<Response> response = deleteLine(lineResponse.getId());

        // then
        assertNoContent(response);
    }

    @DisplayName("구간 정보 조회 기능")
    @Test
    void findSections() {
        createLineRequest(lineRequest1);
        createLineRequest(lineRequest2);
        createLineRequest(lineRequest3);

        ExtractableResponse<Response> response = getLine(1L);

        LineSectionResponse lineSectionResponse = response.as(LineSectionResponse.class);
        List<StationTransferResponse> stationTransferResponses = Arrays.asList(
                new StationTransferResponse(
                        강남역.getId(),
                        강남역.getName(),
                        Arrays.asList(
                                new TransferLineResponse(2L, lineRequest2.getName(), lineRequest2.getColor()),
                                new TransferLineResponse(3L, lineRequest3.getName(), lineRequest3.getColor())
                        )
                ),
                new StationTransferResponse(
                        downStation.getId(),
                        downStation.getName(),
                        Arrays.asList(
                                new TransferLineResponse(2L, lineRequest2.getName(), lineRequest2.getColor())
                        )
                )
        );

        List<SectionResponse> sectionResponses = Arrays.asList(
                SectionResponse.of(
                        new Section(
                                new Station(강남역.getId(), 강남역.getName()),
                                new Station(downStation.getId(), downStation.getName()),
                                new Distance(10)
                        )
                )
        );

        assertThat(lineSectionResponse.getId()).isEqualTo(1L);
        assertThat(lineSectionResponse.getName()).isEqualTo(lineRequest1.getName());
        assertThat(lineSectionResponse.getColor()).isEqualTo(lineRequest1.getColor());
        assertThat(lineSectionResponse.getStations()).usingRecursiveComparison().isEqualTo(stationTransferResponses);
        assertThat(lineSectionResponse.getSections()).usingRecursiveComparison().isEqualTo(sectionResponses);
    }

    @DisplayName("지도를 위한 전체 라인 조회 기능")
    @Test
    void findAllLinesForMap() {
        createLineRequest(lineRequest1);
        createLineRequest(lineRequest2);
        createLineRequest(lineRequest3);

        ExtractableResponse<Response> response = getMap();
        List<LineMapResponse> lineMapResponses = Arrays.asList(response.as(LineMapResponse[].class));

        List<LineMapResponse> expectedLineMapResponses = Arrays.asList(
                new LineMapResponse(
                        1L,
                        lineRequest1.getName(),
                        lineRequest1.getColor(),
                        Arrays.asList(
                                new StationMapResponse(
                                        강남역.getId(),
                                        강남역.getName(),
                                        lineRequest1.getDistance(),
                                        Arrays.asList(
                                                new TransferLineResponse(2L, lineRequest2.getName(), lineRequest2.getColor()),
                                                new TransferLineResponse(3L, lineRequest3.getName(), lineRequest3.getColor())
                                        )
                                ),
                                new StationMapResponse(
                                        downStation.getId(),
                                        downStation.getName(),
                                        DISTANCE_FOR_DOWN_END_STATION,
                                        Arrays.asList(
                                                new TransferLineResponse(2L, lineRequest2.getName(), lineRequest2.getColor())
                                        )
                                )
                        )
                ),
                new LineMapResponse(
                        2L,
                        lineRequest2.getName(),
                        lineRequest2.getColor(),
                        Arrays.asList(
                                new StationMapResponse(
                                        강남역.getId(),
                                        강남역.getName(),
                                        lineRequest2.getDistance(),
                                        Arrays.asList(
                                                new TransferLineResponse(1L, lineRequest1.getName(), lineRequest1.getColor()),
                                                new TransferLineResponse(3L, lineRequest3.getName(), lineRequest3.getColor())
                                        )
                                ),
                                new StationMapResponse(
                                        downStation.getId(),
                                        downStation.getName(),
                                        DISTANCE_FOR_DOWN_END_STATION,
                                        Arrays.asList(
                                                new TransferLineResponse(1L, lineRequest1.getName(), lineRequest1.getColor())
                                        )
                                )
                        )
                ),
                new LineMapResponse(
                        3L,
                        lineRequest3.getName(),
                        lineRequest3.getColor(),
                        Arrays.asList(
                                new StationMapResponse(
                                        강남역.getId(),
                                        강남역.getName(),
                                        lineRequest3.getDistance(),
                                        Arrays.asList(
                                                new TransferLineResponse(1L, lineRequest1.getName(), lineRequest1.getColor()),
                                                new TransferLineResponse(2L, lineRequest2.getName(), lineRequest2.getColor())
                                        )
                                ),
                                new StationMapResponse(
                                        역삼역.getId(),
                                        역삼역.getName(),
                                        DISTANCE_FOR_DOWN_END_STATION,
                                        new ArrayList<>()
                                )
                        )
                )
        );

        assertThat(lineMapResponses).usingRecursiveComparison().isEqualTo(expectedLineMapResponses);
    }

    private ExtractableResponse<Response> getMap() {
        return RestAssured
                .given().log().all()
                .when().get("/map")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getLine(long lineId) {
        return RestAssured
                .given().log().all()
                .when().get("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> getAllLines() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> updateLine(long lineId, LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteLine(long lineId) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private void assertLine(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertOk(response);
        LineResponse resultResponse = response.as(LineResponse.class);

        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    private void assertLines(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
