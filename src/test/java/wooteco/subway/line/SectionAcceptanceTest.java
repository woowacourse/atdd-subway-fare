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
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionAddResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_조회_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    public static void 지하철_구간_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        지하철_구간_생성_요청(lineResponse, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/api/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(StationResponse::getId)
                .collect(toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
                .collect(toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_구간_등록_실패됨_400(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_구간_등록_실패됨_404(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);

        SectionAddResponse addedSection = result.jsonPath().getObject(".", SectionAddResponse.class);
        LineResponse findLine = response.jsonPath().getObject(".", LineResponse.class);

        List<Long> stationIds = findLine.getStations().stream()
                .map(StationResponse::getId)
                .collect(toList());

        assertThat(stationIds.contains(addedSection.getUpStationId())).isTrue();
        assertThat(stationIds.contains(addedSection.getDownStationId())).isTrue();
    }

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        정자역 = 지하철역_등록되어_있음("정자역");
        광교역 = 지하철역_등록되어_있음("광교역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @DisplayName("구간 등록 - 지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 양재역, 3);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("구간 등록 - 지하철 노선에 여러 개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 강남역, 5);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    @DisplayName("구간 등록 - 존재하지 않는 역을 기준으로 등록하는 경우 404 에러가 발생한다.")
    @Test
    void addLineSectionWithNoStation() {
        // given
        StationResponse 탄현역 = new StationResponse(5L, "탄현역");

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 탄현역, 3);

        // then
        지하철_구간_등록_실패됨_404(response);
    }

    @DisplayName("구간 등록 - 구간 거리가 1 이상이 아닌 경우 400 에러가 발생한다.")
    @Test
    void addLineSectionWithNonPositiveDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 광교역, -2);

        // then
        지하철_구간_등록_실패됨_400(response);
    }

    @DisplayName("구간 등록 - 지하철 노선에 이미 존재하는 역을 등록하는 경우 400 에러가 발생한다.")
    @Test
    void addLineSectionWithSameStationInLine() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 광교역, 3);

        // then
        지하철_구간_등록_실패됨_400(response);
    }

    @DisplayName("구간 등록 - 지하철 노선에 존재하지 않는 역을 기준으로 등록하는 경우 400 에러가 발생한다.")
    @Test
    void addLineSectionWithNoStationInLine() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 양재역, 3);

        // then
        지하철_구간_등록_실패됨_400(response);
    }

    @DisplayName("구간 등록 - 기존 구간 길이랑 같은 길이인 경우 400 에러가 발생한다.")
    @Test
    void addLineSectionWithSameDistance() {
        // given
        지하철_구간_생성_요청(신분당선, 강남역, 정자역, 2);

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 양재역, 정자역, 2);

        // then
        지하철_구간_등록_실패됨_400(response);
    }

    @DisplayName("구간 등록 - 기존 구간 길이보다 긴 길이인 경우 400 에러가 발생한다.")
    @Test
    void addLineSectionWithOverDistance() {
        // given
        지하철_구간_생성_요청(신분당선, 강남역, 정자역, 2);

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 양재역, 정자역, 6);

        // then
        지하철_구간_등록_실패됨_400(response);
    }

    @DisplayName("구간 삭제 - 지하철 노선에 등록된 역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2);
        지하철_구간_생성_요청(신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("구간 삭제 - 지하철 노선에 등록된 역이 두 개일 때 한 역을 제외하는 경우 400 에러가 발생한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }
}
