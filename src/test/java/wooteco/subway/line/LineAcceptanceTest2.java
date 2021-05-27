package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest2.loginRequest;

public class LineAcceptanceTest2 extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        jdbcTemplate.update("insert into STATION (name) values ('가양역')");
        jdbcTemplate.update("insert into STATION (name) values ('증미역')");
    }

    @Test
    @DisplayName("유효하지 않은 토큰일 경우의 노선 삽입")
    public void createLineWhenInvalidToken() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 10);
        ExtractableResponse<Response> response = createLineRequest(request, "asdf");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_TOKEN");
    }

    @Test
    @DisplayName("유효하지 않은 이름일 경우의 노선 삽입")
    public void createLineWhenInvalidName() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        LineRequest request = new LineRequest("!@#", "황토색", 1L, 2L, 10);
        ExtractableResponse<Response> response = createLineRequest(request, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_NAME");
    }

    @Test
    @DisplayName("유효하지 않은 색깔일 경우의 노선 삽입")
    public void createLineWhenInvalidColor() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        LineRequest request = new LineRequest("9호선", " ", 1L, 2L, 10);
        ExtractableResponse<Response> response = createLineRequest(request, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_NAME");
    }

    @Test
    @DisplayName("유효하지 않은 거리일 경우의 노선 삽입")
    public void createLineWhenInvalidDistance() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 0);
        ExtractableResponse<Response> response = createLineRequest(request, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_DISTANCE");
    }

    @Test
    @DisplayName("중복되는 노선 이름일 경우의 노선 삽입")
    public void createLineWhenDuplicatedLineName() {
        jdbcTemplate.update("insert into LINE (name, color, extraFare) values ('9호선', '황토색', 30)");
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        ExtractableResponse<Response> response = createLineRequest(request, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("DUPLICATED_LINE_NAME");
    }

    @Test
    @DisplayName("존재하지 않는 역이 포함된 경우의 노선 삽입")
    public void createLineWhenNonExistStation() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        LineRequest request = new LineRequest("9호선", "황토색", 10L, 2L, 30);
        ExtractableResponse<Response> response = createLineRequest(request, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("NO_SUCH_STATION");
    }

    @Test
    @DisplayName("상행 하행역이 같은 경우의 노선 삽입")
    public void createLineWhenSameBothStations() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 1L, 30);
        ExtractableResponse<Response> response = createLineRequest(request, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("SAME_STATIONS_IN_SAME_SECTION");
    }

    @Test
    @DisplayName("정상적인 노선 삽입")
    public void createLine() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        ExtractableResponse<Response> response = createLineRequest(request, accessToken);

        LineResponse lineResponse = response.body().as(LineResponse.class);
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat((String) response.jsonPath().get("name")).isEqualTo("9호선");
        assertThat((String) response.jsonPath().get("color")).isEqualTo("황토색");
    }

    @Test
    @DisplayName("유효하지 않은 토큰일 경우의 모든 노선 목록")
    public void totalLineResponseWhenInvalidToken() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);
        ExtractableResponse<Response> response = findAllRequest("asdf");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_TOKEN");
    }

    @Test
    @DisplayName("정상적인 모든 노선 목록")
    public void totalLineResponse() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);
        ExtractableResponse<Response> response = findAllRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getList("id").get(0)).isEqualTo(1);
        assertThat(response.jsonPath().getList("name").get(0)).isEqualTo("9호선");
        assertThat(response.jsonPath().getList("color").get(0)).isEqualTo("황토색");
        assertThat(response.jsonPath().getList("startStation.id").get(0)).isEqualTo(1);
        assertThat(response.jsonPath().getList("endStation.id").get(0)).isEqualTo(2);
        assertThat(response.jsonPath().getList("distance").get(0)).isEqualTo(30);
    }

    @Test
    @DisplayName("존재하지 않는 노선에 대한 id를 통한 노선 조회")
    public void findByIdWhenNotExist() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = findByIdRequest(1L, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("NO_SUCH_LINE");
    }

    @Test
    @DisplayName("정상적인 id를 통한 노선 조회")
    public void findById() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);
        ExtractableResponse<Response> response = findByIdRequest(1L, accessToken);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat((String) response.jsonPath().get("name")).isEqualTo("9호선");
        assertThat((String) response.jsonPath().get("color")).isEqualTo("황토색");
    }

    @Test
    @DisplayName("중복되는 이름으로 노선 수정")
    public void updateLineWhenDuplicatedName() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);
        LineRequest request2 = new LineRequest("8호선", "초록색", 1L, 2L, 30);
        createLineRequest(request2, accessToken);

        LineRequest newlyRequest = new LineRequest("9호선", "초록색", 1L, 2L, 30);
        ExtractableResponse<Response> response = updateLineRequest(2L, newlyRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("DUPLICATED_LINE_NAME");
    }

    @Test
    @DisplayName("정상적인 노선 수정")
    public void updateLine() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);

        LineRequest newlyRequest = new LineRequest("9호선", "초록색", 1L, 2L, 30);
        ExtractableResponse<Response> response = updateLineRequest(1L, newlyRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("존재하지 않는 노선을 삭제")
    public void deleteLineWhenNotExist() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = deleteLineRequest(1L, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("NO_SUCH_LINE");
    }

    @Test
    @DisplayName("정상적인 노선 삭제")
    public void deleteLine() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);

        ExtractableResponse<Response> response = deleteLineRequest(1L, accessToken);

        Boolean result = jdbcTemplate.queryForObject("select exists (select * from LINE where name = '9호선')", Boolean.class);
        assertThat(response.statusCode()).isEqualTo(204);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("두 역 다 이미 노선에 존재하는 경우의 구간 추가")
    public void addSectionWhenBothStationsIncludeInLine() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);

        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        ExtractableResponse<Response> response = addLineStationRequest(1L, sectionRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("BOTH_STATION_ALREADY_REGISTERED_TO_LINE");
    }

    @Test
    @DisplayName("두 역 다 노선에 존재하지 않는 경우의 구간 추가")
    public void addSectionWhenBothStationsNotIncludeInLine() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);
        jdbcTemplate.update("insert into STATION (name) values ('third')");
        jdbcTemplate.update("insert into STATION (name) values ('fourth')");

        SectionRequest sectionRequest = new SectionRequest(3L, 4L, 10);
        ExtractableResponse<Response> response = addLineStationRequest(1L, sectionRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("BOTH_STATION_NOT_REGISTERED_IN_LINE");
    }

    @Test
    @DisplayName("구간 거리가 0 일 때의 구간 추가")
    public void addSectionWhenInvalidDistance() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);

        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 0);
        ExtractableResponse<Response> response = addLineStationRequest(1L, sectionRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_DISTANCE");
    }

    @Test
    @DisplayName("중간 삽입인데 그 거리가 기존의 구간보다 높거나 같은 경우의 구간 삽입")
    public void addSectionWhenInvalidDistance2() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);
        jdbcTemplate.update("insert into STATION (name) values ('third')");

        SectionRequest sectionRequest = new SectionRequest(1L, 3L, 30);
        ExtractableResponse<Response> response = addLineStationRequest(1L, sectionRequest, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("IMPOSSIBLE_DISTANCE");
    }

    @Test
    @DisplayName("정상적인 구간 추가")
    public void addSection() {
        LineRequest request = new LineRequest("9호선", "황토색", 1L, 2L, 30);
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        createLineRequest(request, accessToken);
        jdbcTemplate.update("insert into STATION (name) values ('third')");
        SectionRequest sectionRequest = new SectionRequest(1L, 3L, 10);
        addLineStationRequest(1L, sectionRequest, accessToken);

        Integer distance = jdbcTemplate.queryForObject("select distance from SECTION where line_id = 1 and up_station_id = 1 and down_station_id =3", Integer.class);
        assertThat(distance).isEqualTo(10);
        Integer distance2 = jdbcTemplate.queryForObject("select distance from SECTION where line_id = 1 and up_station_id = 3 and down_station_id =2", Integer.class);
        assertThat(distance2).isEqualTo(20);
    }

    private ExtractableResponse<Response> createLineRequest(LineRequest lineRequest, String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(lineRequest)
                .when()
                .post("/api/lines")
                .then().extract();
    }

    private ExtractableResponse<Response> findAllRequest(String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when()
                .get("/api/lines")
                .then().extract();
    }

    private ExtractableResponse<Response> findByIdRequest(Long id, String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when()
                .get("/api/lines/" + id)
                .then().extract();
    }

    private ExtractableResponse<Response> updateLineRequest(Long id, LineRequest lineRequest, String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .auth().oauth2(accessToken)
                .when()
                .put("/api/lines/" + id)
                .then().extract();
    }

    private ExtractableResponse<Response> deleteLineRequest(Long id, String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when()
                .delete("/api/lines/" + id)
                .then().extract();
    }

    private ExtractableResponse<Response> addLineStationRequest(Long lineId, SectionRequest request, String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when()
                .post("/api/lines/" + lineId + "/sections")
                .then().extract();
    }
}
