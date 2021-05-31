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
import wooteco.subway.line.dto.LineWithStationsResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.dto.SectionResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(성인_사용자, 신분당선, 강남역, 양재역, 3);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("등록 - 로그인하지 않은 사용자나 유효하지 않은 회원이 요청시 예외를 발생한다.")
    @Test
    void addLineSectionWhenNotValidMember() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(비회원, 신분당선, 강남역, 양재역, 3);

        // then
        비회원_요청_실패됨(response);
    }

    @DisplayName("등록 - 존재하지 않는 역인 경우 예외를 던진다.")
    @Test
    void addLineSectionWhenNotExistsStation() {
        // when
        StationResponse 없는역 = new StationResponse(11L, "없는역");
        StationResponse 없는역2 = new StationResponse(12L, "없는역2");
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(성인_사용자, 신분당선, 없는역, 없는역2, 3);

        // then
        assertThat(response.as(ExceptionResponse.class).getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("등록 - 유효하지 않은 거리의 경우 예외를 던진다.")
    @Test
    void addLineSectionWhenNotValidDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(성인_사용자, 신분당선, 강남역, 양재역, 0);

        // then
        assertThat(response.as(ExceptionResponse.class).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("등록 - 등록하려는 구간의 거리가 기존 구간보다 긴 경우 예외를 던진다.")
    @Test
    void addLineSectionWhenLongerDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(성인_사용자, 신분당선, 강남역, 양재역, 11);

        // then
        assertThat(response.as(ExceptionResponse.class).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_구간_생성_요청(성인_사용자, 신분당선, 강남역, 양재역, 2);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(성인_사용자, 신분당선, 정자역, 강남역, 3);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    @DisplayName("등록 - 지하철 노선에 이미 등록되어있는 역을 등록 요청시 예외를 던진다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(성인_사용자, 신분당선, 강남역, 광교역, 3);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(성인_사용자, 신분당선, 정자역, 양재역, 3);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_구간_생성_요청(성인_사용자, 신분당선, 강남역, 양재역, 2);
        지하철_구간_생성_요청(성인_사용자, 신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(성인_사용자, 신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("제거 - 로그인하지 않은 사용자나 유효하지 않은 회원이 요청시 예외를 발생한다.")
    @Test
    void removeLineSectionWhenNotValidMember() {
        // given
        지하철_구간_생성_요청(성인_사용자, 신분당선, 강남역, 양재역, 2);
        지하철_구간_생성_요청(성인_사용자, 신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(비회원, 신분당선, 양재역);

        // then
        비회원_요청_실패됨(removeResponse);
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(성인_사용자, 신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }

    public static void 지하철_구간_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        지하철_구간_생성_요청(성인_사용자, lineResponse, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(TokenResponse token, LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/api/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.as(SectionResponse.class).getDistance()).isEqualTo(3);

        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineWithStationsResponse line = response.as(LineWithStationsResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
}

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(TokenResponse token, LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_구간_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
