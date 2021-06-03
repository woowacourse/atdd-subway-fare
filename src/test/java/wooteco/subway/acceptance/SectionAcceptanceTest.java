package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.web.dto.request.SectionRequest;
import wooteco.subway.web.dto.response.LineResponse;
import wooteco.subway.web.dto.response.StationResponse;
import wooteco.subway.web.dto.response.StationWithDistanceResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.auth.acceptance.AuthAcceptanceTest.토큰;
import static wooteco.subway.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.acceptance.LineAcceptanceTest.지하철_노선_조회_요청;
import static wooteco.subway.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;

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

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        정자역 = 지하철역_등록되어_있음("정자역");
        광교역 = 지하철역_등록되어_있음("광교역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 양재역, 3);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(
                new StationWithDistanceResponse(1L, "강남역", 3),
                new StationWithDistanceResponse(2L, "양재역", 7),
                new StationWithDistanceResponse(4L, "광교역")
        ));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 강남역, 5);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(
                new StationWithDistanceResponse(3L, "정자역", 5),
                new StationWithDistanceResponse(1L, "강남역", 2),
                new StationWithDistanceResponse(2L, "양재역", 8),
                new StationWithDistanceResponse(4L, "광교역")
        ));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 광교역, 3);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 양재역, 3);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2);
        지하철_구간_생성_요청(신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, Arrays.asList(
                new StationWithDistanceResponse(1L, "강남역", 4),
                new StationWithDistanceResponse(3L, "정자역", 6),
                new StationWithDistanceResponse(4L, "광교역")
        ));
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }


    public static void 지하철_구간_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        지하철_구간_생성_요청(lineResponse, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰().getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/api/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationWithDistanceResponse> stationResponses) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(StationWithDistanceResponse::getId).collect(Collectors.toList());
        List<Long> expectedStationIds = stationResponses.stream()
                .map(StationWithDistanceResponse::getId)
                .collect(Collectors.toList());
        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰().getAccessToken())
                .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_구간_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationWithDistanceResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationWithDistanceResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_구간_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}