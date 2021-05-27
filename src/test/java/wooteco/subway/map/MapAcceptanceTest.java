package wooteco.subway.map;

import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationResponse;

public class MapAcceptanceTest extends AcceptanceTest {

    public static ExtractableResponse<Response> 전체보기_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/map")
            .then().log().all()
            .extract();
    }

    @BeforeEach
    public void setUp() {
        super.setUp();

        StationResponse 강남역 = 지하철역_등록되어_있음("강남역");
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역");
        StationResponse 정자역 = 지하철역_등록되어_있음("정자역");
        StationResponse 광교역 = 지하철역_등록되어_있음("광교역");

        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역,
            10);
        LineResponse 이호선 = 지하철_노선_등록되어_있음("2호선", "bg-red-600", 강남역, 정자역,
            20);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음("3호선", "bg-red-600", 강남역, 정자역,
            20);

        지하철_구간_등록되어_있음(신분당선, 양재역, 정자역, 3);
        지하철_구간_등록되어_있음(신분당선, 정자역, 광교역, 3);
        지하철_구간_등록되어_있음(이호선, 정자역, 광교역, 11);
    }

    @DisplayName("전체보기")
    @Test
    public void map() {
        //fixme 테스트코드 추가 필요
        전체보기_조회_요청();
    }
}
