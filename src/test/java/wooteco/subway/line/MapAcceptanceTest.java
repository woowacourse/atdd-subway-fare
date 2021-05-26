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
import wooteco.subway.line.ui.dto.LineRequest;
import wooteco.subway.line.ui.dto.map.StationOfMapResponse;
import wooteco.subway.station.ui.dto.StationResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

public class MapAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        광교역 = 지하철역_등록되어_있음("광교역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 0);
        지하철_노선_생성되어_있음(lineRequest1);
    }

    @DisplayName("전체 노선도 보기")
    @Test
    void findMap() {
        // when
        ExtractableResponse<Response> response = 전체_노선도_조회();
        System.out.println(response.asString());
        List<StationOfMapResponse> stations = response.jsonPath()
            .getList("[0].stations", StationOfMapResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong("[0].id")).isEqualTo(1L);
        assertThat(response.jsonPath().getString("[0].name")).isEqualTo(lineRequest1.getName());
        assertThat(response.jsonPath().getString("[0].color")).isEqualTo(lineRequest1.getColor());

        assertThat(stations).usingRecursiveComparison()
            .isEqualTo(Arrays.asList(
                new StationOfMapResponse(강남역.getId(), 강남역.getName(), 10, Collections.emptyList()),
                new StationOfMapResponse(광교역.getId(), 광교역.getName(), -1, Collections.emptyList())
            ));
    }

    private ExtractableResponse<Response> 전체_노선도_조회() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/map")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성되어_있음(LineRequest params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all().
                extract();
    }
}

