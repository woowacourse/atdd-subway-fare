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
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.*;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_조회_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private static String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);
        accessToken = tokenResponse.getAccessToken();

        강남역 = 지하철역_등록되어_있음("강남역", accessToken);
        양재역 = 지하철역_등록되어_있음("양재역", accessToken);
        정자역 = 지하철역_등록되어_있음("정자역", accessToken);
        광교역 = 지하철역_등록되어_있음("광교역", accessToken);

        신분당선 = 지하철_노선_등록되어_있음(accessToken, "신분당선", "bg-red-600", 강남역, 광교역, 10, 1000);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(accessToken, 신분당선, 강남역, 양재역, 3);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("같은 역을 하나의 지하철 구간으로 등록한다.")
    @Test
    void addSameStationsInSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(accessToken, 신분당선, 강남역, 강남역, 3);

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("SAME_STATIONS_IN_SAME_SECTION");
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_구간_생성_요청(accessToken, 신분당선, 강남역, 양재역, 2);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(accessToken, 신분당선, 정자역, 강남역, 5);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(accessToken, 신분당선, 강남역, 광교역, 3);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("BOTH_STATION_ALREADY_REGISTERED_IN_LINE");
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 구간을 생성한다.")
    @Test
    void createLineWithoutToken() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청("accessToken", 신분당선, 강남역, 광교역, 3);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("INVALID_TOKEN");
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(accessToken, 신분당선, 정자역, 양재역, 3);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("BOTH_STATION_NOT_REGISTERED_IN_LINE");
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_구간_생성_요청(accessToken, 신분당선, 강남역, 양재역, 2);
        지하철_구간_생성_요청(accessToken, 신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        assertThat(removeResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = removeResponse.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("ONLY_ONE_SECTION_EXIST");
    }

    @DisplayName("옳지 않은 거리의 입력으로 구간을 생성한다.")
    @Test
    void createSectionWithInvalidDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(accessToken, 신분당선, 강남역, 양재역, 0);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("INVALID_DISTANCE");
    }

    @DisplayName("이미 존재하는 구간보다 긴 구간 거리로 구간을 생성한다.")
    @Test
    void createSectionWithImpossibleDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(accessToken, 신분당선, 강남역, 양재역, 10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("IMPOSSIBLE_DISTANCE");
    }

    public static void 지하철_구간_등록되어_있음(String accessToken, LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        지하철_구간_생성_요청(accessToken, lineResponse, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(String accessToken, LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .auth().oauth2(accessToken)
                .when().post("/api/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(accessToken, lineResponse.getId());
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(accessToken, lineResponse.getId());
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }
}
