package wooteco.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.loginRequest;

public class StationAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("유효하지 않은 토큰일 때의 역 삽입")
    public void createStationWhenInvalidToken() {
        ExtractableResponse<Response> response = createStationRequest(new StationRequest("가양역"), "asdf");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_TOKEN");
    }

    @Test
    @DisplayName("유효하지 않은 역 명일 때의 역 삽입")
    public void createStationWhenInvalidStationName() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = createStationRequest(new StationRequest("!@#$$"), accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_NAME");
    }

    @Test
    @DisplayName("중복 역 명일 때의 역 삽입")
    public void createStationWhenInvalidDuplicatedStationName() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        jdbcTemplate.update("insert into STATION (name) values ('가양역')");
        ExtractableResponse<Response> response = createStationRequest(new StationRequest("가양역"), accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("DUPLICATED_STATION_NAME");
    }

    @Test
    @DisplayName("정상적인 역 삽입")
    public void createStation() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = createStationRequest(new StationRequest("가양역"), accessToken);

        assertThat(response.statusCode()).isEqualTo(201);
        StationResponse stationResponse = response.body().as(StationResponse.class);
        assertThat(stationResponse).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new StationResponse(null, "가양역"));
    }

    @Test
    @DisplayName("유효하지 않은 토큰일 때의 역 목록")
    public void showStationsWhenInvalidToken() {
        ExtractableResponse<Response> response = showStationsRequest("asdf");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_TOKEN");
    }

    @Test
    @DisplayName("정상적인 역 목록")
    public void showStations() {
        jdbcTemplate.update("insert into STATION (name) values ('가양역')");
        jdbcTemplate.update("insert into STATION (name) values ('증미역')");

        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = showStationsRequest(accessToken);
        List<String> names = response.jsonPath().getList("name");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(names).containsExactlyInAnyOrder("가양역", "증미역");
    }

    @Test
    @DisplayName("유효하지 않은 토큰일 때의 역 삭제")
    public void deleteStationWhenInvalidToken() {
        jdbcTemplate.update("insert into STATION (name) values ('가양역')");

        ExtractableResponse<Response> response = deleteStationsRequest(1L, "asdf");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("INVALID_TOKEN");
    }

    @Test
    @DisplayName("역이 없을 경우의 역 삭제")
    public void deleteStationWhenStationNotExist() {
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = deleteStationsRequest(1L, accessToken);

        assertThat(response.statusCode()).isEqualTo(400);
        assertThat((String) response.jsonPath().get("error")).isEqualTo("NO_SUCH_STATION");
    }

    @Test
    @DisplayName("정상적인 역 삭제")
    public void deleteStation() {
        jdbcTemplate.update("insert into STATION (name) values ('가양역')");
        String accessToken = loginRequest(new TokenRequest("email@email.com", "password")).jsonPath().get("accessToken");
        ExtractableResponse<Response> response = deleteStationsRequest(1L, accessToken);

        assertThat(response.statusCode()).isEqualTo(204);
    }

    private ExtractableResponse<Response> createStationRequest(StationRequest request, String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when()
                .post("/api/stations")
                .then().extract();
    }

    private ExtractableResponse<Response> showStationsRequest(String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when()
                .get("/api/stations")
                .then().extract();
    }

    private ExtractableResponse<Response> deleteStationsRequest(Long id, String accessToken) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when()
                .delete("/api/stations/" + id)
                .then().extract();
    }
}
