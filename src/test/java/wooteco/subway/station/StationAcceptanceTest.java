package wooteco.subway.station;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class StationAcceptanceTest extends AcceptanceTest {

    private String loginToken;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    protected void setUp() {
        super.setUp();
        registerMember("kevin@naver.com", "123", 123);
        loginToken = login("kevin@naver.com", "123").getAccessToken();
    }

    @DisplayName("지하철 역을 생성한다.")
    @Test
    void createStationSuccessfully() throws JsonProcessingException {
        ExtractableResponse<Response> response = createStation("강남역", loginToken);

        assertResponseStatus(response, HttpStatus.CREATED);
        assertResponseMessage(response, objectMapper.writeValueAsString(response.as(StationResponse.class)));
    }

    @DisplayName("이름이 중복된 경우 실패한다.")
    @Test
    void cannotCreateDuplicatedStation() {
        createStation("강남역", loginToken);

        ExtractableResponse<Response> response = createStation("강남역", loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "존재하는 역 이름입니다.");
    }

    @DisplayName("이름이 공백이거나 2-20글자가 아니면 실패한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"역", "역역역역역역역역역역역역역역역역역역역역역"})
    void invalidNameFormat(String name) {
        ExtractableResponse<Response> response = createStation(name, loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "잘못된 역 이름입니다.");
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStation() {
        StationResponse response1 = createStation("강남역", loginToken).as(StationResponse.class);
        StationResponse response2 = createStation("청담역", loginToken).as(StationResponse.class);

        ExtractableResponse<Response> stationsResponse = findStationRequest();

        assertResponseStatus(stationsResponse, HttpStatus.OK);
        assertThatStationsIncluded(stationsResponse, Arrays.asList(response1, response2));
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void editStation() {
        StationResponse stationResponse = createStation("강남역", loginToken).as(StationResponse.class);
        StationRequest stationRequest = new StationRequest("청담역");

        ExtractableResponse<Response> response = editStation(stationResponse.getId(), stationRequest);

        assertResponseStatus(response, HttpStatus.OK);
    }

    @DisplayName("지하철역 수정시 중복된 이름으로 수정할 수 없다.")
    @Test
    void cannotEditStation() {
        StationResponse stationResponse = createStation("강남역", loginToken).as(StationResponse.class);
        createStation("청담역", loginToken);
        StationRequest stationRequest = new StationRequest("청담역");

        ExtractableResponse<Response> response = editStation(stationResponse.getId(), stationRequest);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "존재하는 역 이름입니다.");
    }

    @DisplayName("지하철역 수정시 id에 해당하는 지하철역이 존재하지 않으면 수정할 수 없다.")
    @Test
    void cannotFindStationWhenEditing() {
        StationRequest stationRequest = new StationRequest("청담역");

        ExtractableResponse<Response> response = editStation(31L, stationRequest);

        assertResponseStatus(response, HttpStatus.NOT_FOUND);
        assertResponseMessage(response, "존재하지 않는 역입니다.");
    }

    @DisplayName("지하철 역을 삭제한다.")
    @Test
    void deleteStation() {
        StationResponse stationResponse = createStation("강남역", loginToken).as(StationResponse.class);

        ExtractableResponse<Response> response = deleteStation(stationResponse.getId());

        assertResponseStatus(response, HttpStatus.NO_CONTENT);
    }

    @DisplayName("지하철역 삭제시 id에 해당하는 지하철역이 존재하지 않으면 삭할 수 없다.")
    @Test
    void cannotDeleteStation() {
        ExtractableResponse<Response> response = deleteStation(31);

        assertResponseStatus(response, HttpStatus.NOT_FOUND);
        assertResponseMessage(response, "존재하지 않는 역입니다.");
    }

    @DisplayName("지하철역 삭제시 노선에 등록된 지하철역은 삭제할 수 없다.")
    @Test
    void cannotDeleteRegisteredStation() {
        StationResponse stationResponse1 = createStation("강남역", loginToken).as(StationResponse.class);
        StationResponse stationResponse2 = createStation("청담역", loginToken).as(StationResponse.class);
        LineRequest lineRequest = new LineRequest("1호선", "black", stationResponse1.getId(), stationResponse2.getId(), 10);
        createLine(lineRequest, loginToken);

        ExtractableResponse<Response> response = deleteStation(stationResponse1.getId());

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "이미 노선에 등록된 역은 삭제할 수 없습니다.");
    }

    private ExtractableResponse<Response> findStationRequest() {
        return RestAssured.given().log().all()
                .auth().oauth2(loginToken)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> editStation(long id, StationRequest stationRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .auth().oauth2(loginToken)
                .when().put("/stations/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteStation(long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(loginToken)
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }

    private void assertThatStationsIncluded(ExtractableResponse<Response> response, List<StationResponse> stations) {
        List<Long> expectedLineIds = stations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
