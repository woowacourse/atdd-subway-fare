package wooteco.subway.line;

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
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SimpleLineResponse;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.util.TestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.station.StationAcceptanceTest.createStation;
import static wooteco.subway.util.TestUtil.assertResponseMessage;
import static wooteco.subway.util.TestUtil.assertResponseStatus;

public class LineAcceptanceTest extends AcceptanceTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private StationResponse firstStation;
    private StationResponse secondStation;
    private LineRequest firstLineRequest;
    private LineRequest secondLineRequest;
    private String loginToken;

    public static ExtractableResponse<Response> createLine(LineRequest params, String token) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findOneLine(long id, String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private void assertThatLinesIncluded(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", SimpleLineResponse.class).stream()
                .map(SimpleLineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> findLineListRequest() {
        return RestAssured.given().log().all()
                .auth().oauth2(loginToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> editLine(long id, LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .auth().oauth2(loginToken)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteLine(long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(loginToken)
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        TestUtil.registerMember("kevin@naver.com", "123", 123);
        loginToken = TestUtil.login("kevin@naver.com", "123").getAccessToken();

        firstStation = createStation("강남역", loginToken).as(StationResponse.class);
        secondStation = createStation("광교역", loginToken).as(StationResponse.class);

        firstLineRequest = new LineRequest("신분당선", "bg-red-600", firstStation.getId(), secondStation.getId(), 900, 10);
        secondLineRequest = new LineRequest("1호선", "bg-red-602", firstStation.getId(), secondStation.getId(), 900, 15);
    }

    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {
        ExtractableResponse<Response> response = createLine(firstLineRequest, loginToken);

        assertResponseStatus(response, HttpStatus.CREATED);
    }

    @DisplayName("중복된 이름의 노선은 생성할 수 없다.")
    @Test
    void cannotCreateDuplicatedNameLine() {
        createLine(firstLineRequest, loginToken);

        ExtractableResponse<Response> response = createLine(firstLineRequest, loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "존재하는 노선 이름입니다.");
    }

    @DisplayName("노선 요청 이름은 공백이 아니어야 하며, 2-20자여야 한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "선", "선선선선선선선선선선선선선선선선선선선선선선선선선"})
    void cannotCreateDuplicatedNameLine(String name) {
        LineRequest lineRequest = new LineRequest(name, "newcolor", firstStation.getId(), secondStation.getId(), 900, 10);

        ExtractableResponse<Response> response = createLine(lineRequest, loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "잘못된 노선 이름입니다.");
    }

    @DisplayName("노선 요청 구간 거리는 1 이상이어야 한다.")
    @Test
    void cannotCreateInvalidDistanceLine() {
        LineRequest lineRequest = new LineRequest("39호선", "newcolor", firstStation.getId(), secondStation.getId(), 900, 0);

        ExtractableResponse<Response> response = createLine(lineRequest, loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "잘못된 노선 구간 거리입니다.");
    }

    @DisplayName("노선 등록시 색상은 중복되어서 안된다.")
    @Test
    void cannotCreateDuplicatedColorLine() {
        LineRequest duplicatedColorLine = new LineRequest("39호선", "bg-red-600", firstStation.getId(), secondStation.getId(), 900, 10);

        createLine(firstLineRequest, loginToken);
        ExtractableResponse<Response> response = createLine(duplicatedColorLine, loginToken);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "잘못된 노선 색상입니다.");
    }

    @DisplayName("노선 등록시 지하철 역은 존재해야 한다.")
    @Test
    void stationsMustBeValid() {
        LineRequest lineRequest = new LineRequest("39호선", "bg-red-63", firstStation.getId(), 99L, 900, 10);

        ExtractableResponse<Response> response = createLine(lineRequest, loginToken);

        assertResponseStatus(response, HttpStatus.NOT_FOUND);
        assertResponseMessage(response, "존재하지 않는 역입니다.");
    }

    @DisplayName("노선을 개별 조회한다.")
    @Test
    void findOneLine() throws JsonProcessingException {
        LineResponse lineResponse = createLine(firstLineRequest, loginToken).as(LineResponse.class);

        ExtractableResponse<Response> response = findOneLine(lineResponse.getId(), loginToken);

        assertResponseStatus(response, HttpStatus.OK);
        assertResponseMessage(response, objectMapper.writeValueAsString(lineResponse));
    }

    @DisplayName("노선 조회시 없는 아이디의 노선은 조회할 수 없다.")
    @Test
    void cannotFindOneLine() {
        ExtractableResponse<Response> response = findOneLine(369, loginToken);

        assertResponseStatus(response, HttpStatus.NOT_FOUND);
        assertResponseMessage(response, "존재하지 않는 노선입니다.");
    }

    @DisplayName("간단한 노선 목록을 조회한다.")
    @Test
    void findLineList() {
        LineResponse firstLineResponse = createLine(firstLineRequest, loginToken).as(LineResponse.class);
        LineResponse secondLineResponse = createLine(secondLineRequest, loginToken).as(LineResponse.class);

        ExtractableResponse<Response> response = findLineListRequest();

        assertResponseStatus(response, HttpStatus.OK);
        assertThatLinesIncluded(response, Arrays.asList(firstLineResponse, secondLineResponse));
    }

    @DisplayName("노선 수정시 id에 해당하는 노선이 존재해야 한다.")
    @Test
    void cannotEditLineWhenIdInvalid() {
        LineRequest changeRequest = new LineRequest("경의중앙선", "newcolor", 0L, 0L, 0);

        ExtractableResponse<Response> response = editLine(999, changeRequest);

        assertResponseStatus(response, HttpStatus.NOT_FOUND);
        assertResponseMessage(response, "존재하지 않는 노선입니다.");
    }

    @DisplayName("노선 수정시 수정하려는 이름이 중복되어 있으면 안 된다.")
    @Test
    void cannotEditLineAsDuplicatedName() {
        LineResponse lineResponse = createLine(firstLineRequest, loginToken).as(LineResponse.class);
        createLine(secondLineRequest, loginToken);
        LineRequest changeRequest = new LineRequest("1호선", "newcolor", 0L, 0L, 0);

        ExtractableResponse<Response> response = editLine(lineResponse.getId(), changeRequest);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "존재하는 노선 이름입니다.");
    }

    @DisplayName("노선 수정시 수정하려는 색상이 중복되어 있으면 안 된다.")
    @Test
    void cannotEditLineAsDuplicatedColor() {
        LineResponse lineResponse = createLine(firstLineRequest, loginToken).as(LineResponse.class);
        createLine(secondLineRequest, loginToken);
        LineRequest changeRequest = new LineRequest("9호선", "bg-red-602", 0L, 0L, 0);

        ExtractableResponse<Response> response = editLine(lineResponse.getId(), changeRequest);

        assertResponseStatus(response, HttpStatus.BAD_REQUEST);
        assertResponseMessage(response, "잘못된 노선 색상입니다.");
    }

    @DisplayName("노선 삭제시 삭제하려는 아이디가 존재해야 한다.")
    @Test
    void cannotDeleteLineWhenInvalidId() {
        ExtractableResponse<Response> response = deleteLine(370);

        assertResponseStatus(response, HttpStatus.NOT_FOUND);
        assertResponseMessage(response, "존재하지 않는 노선입니다.");
    }

    @DisplayName("노선이 정상적으로 삭제된다.")
    @Test
    void deleteLine() {
        LineResponse lineResponse = createLine(firstLineRequest, loginToken).as(LineResponse.class);

        ExtractableResponse<Response> response = deleteLine(lineResponse.getId());

        assertResponseStatus(response, HttpStatus.NO_CONTENT);
    }
}
