package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.util.TestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.createLine;
import static wooteco.subway.line.LineAcceptanceTest.findOneLine;
import static wooteco.subway.station.StationAcceptanceTest.createStation;
import static wooteco.subway.util.TestUtil.assertResponseMessage;
import static wooteco.subway.util.TestUtil.assertResponseStatus;

public class SectionAcceptanceTest extends AcceptanceTest {

    private LineResponse lineResponse;
    private StationResponse station1;
    private StationResponse station2;
    private StationResponse station3;
    private StationResponse station4;
    private String loginToken;

    public static ExtractableResponse<Response> addSection(Long id, SectionRequest sectionRequest, String loginToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .auth().oauth2(loginToken)
                .when().post("/lines/{lineId}/sections", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteSection(long lineId, long stationId) {
        return RestAssured.given().log().all()
                .auth().oauth2(loginToken)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", lineId, stationId)
                .then().log().all()
                .extract();

    }

    private void assertOrderedStations(long id, List<StationResponse> stationResponses) {
        LineResponse lineResponse = findOneLine(id, loginToken)
                .as(LineResponse.class);
        List<Long> stationIds = lineResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedStationIds = stationResponses.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        TestUtil.registerMember("kevin@naver.com", "123", 123);
        loginToken = TestUtil.login("kevin@naver.com", "123").getAccessToken();

        station1 = createStation("강남역", loginToken).as(StationResponse.class);
        station2 = createStation("양재역", loginToken).as(StationResponse.class);
        station3 = createStation("정자역", loginToken).as(StationResponse.class);
        station4 = createStation("광교역", loginToken).as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", station1.getId(), station4.getId(), 10);
        lineResponse = createLine(lineRequest, loginToken)
                .as(LineResponse.class);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addSection() {
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station2.getId(), 3);

        ExtractableResponse<Response> response = addSection(lineResponse.getId(), sectionRequest, loginToken);

        assertResponseStatus(response, HttpStatus.OK);
        assertOrderedStations(lineResponse.getId(), Arrays.asList(station1, station2, station4));
    }

    @DisplayName("지하철 노선에 여러 개의 역을 순서 상관없이 등록한다.")
    @Test
    void addSectionRegardlessOfOrder() {
        SectionRequest sectionRequest1 = new SectionRequest(station1.getId(), station2.getId(), 3);
        SectionRequest sectionRequest2 = new SectionRequest(station3.getId(), station1.getId(), 5);
        addSection(lineResponse.getId(), sectionRequest1, loginToken);

        ExtractableResponse<Response> response = addSection(lineResponse.getId(), sectionRequest2, loginToken);

        assertResponseStatus(response, HttpStatus.OK);
        assertOrderedStations(lineResponse.getId(), Arrays.asList(station3, station1, station2, station4));
    }

    @DisplayName("지하철 노선에 이미 등록되어 있는 구간을 등록할 수 없다.")
    @Test
    void cannotAddSectionWhenAlreadyExists() {
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station4.getId(), 4);

        ExtractableResponse<Response> response = addSection(lineResponse.getId(), sectionRequest, loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "구간 추가에 필요한 정보가 잘못되었습니다.");
    }

    @DisplayName("지하철 노선에 등록되지 않은 역들로 구성된 구간을 등록할 수 없다.")
    @Test
    void cannotAddSectionWhenNotConnected() {
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 3);

        ExtractableResponse<Response> response = addSection(lineResponse.getId(), sectionRequest, loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "구간 추가에 필요한 정보가 잘못되었습니다.");
    }

    @DisplayName("지하철 노선에 등록된 구간을 삭제한다.")
    @Test
    void deleteSection() {
        SectionRequest sectionRequest1 = new SectionRequest(station1.getId(), station2.getId(), 3);
        SectionRequest sectionRequest2 = new SectionRequest(station3.getId(), station1.getId(), 5);
        addSection(lineResponse.getId(), sectionRequest1, loginToken);
        addSection(lineResponse.getId(), sectionRequest2, loginToken);

        ExtractableResponse<Response> response = deleteSection(lineResponse.getId(), station1.getId());

        assertResponseStatus(response, HttpStatus.OK);
        assertOrderedStations(lineResponse.getId(), Arrays.asList(station3, station2, station4));
    }

    @DisplayName("구간이 1개밖에 없는 노선의 구간을 삭제할 수 없다.")
    @Test
    void cannotDeleteOnlyOneSection() {
        ExtractableResponse<Response> response = deleteSection(lineResponse.getId(), station1.getId());

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "노선에는 최소한 하나의 구간은 존재해야합니다.");
    }
}
