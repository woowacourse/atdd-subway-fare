package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음_withToken;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_조회_요청;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음_withToken;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 10;
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private TokenResponse 사용자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        사용자 = 로그인되어_있음(EMAIL, PASSWORD);

        강남역 = 지하철역_등록되어_있음_withToken(사용자, "강남역");
        양재역 = 지하철역_등록되어_있음_withToken(사용자, "양재역");
        정자역 = 지하철역_등록되어_있음_withToken(사용자, "정자역");
        광교역 = 지하철역_등록되어_있음_withToken(사용자, "광교역");

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음_withToken(사용자, lineRequest);
    }

    @DisplayName("구간 추가 - 구간 지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청_withToken(사용자, 신분당선, 강남역, 양재역, 3);

        // then
        지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("구간 추가 - 지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_구간_생성_요청_withToken(사용자, 신분당선, 강남역, 양재역, 2);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청_withToken(사용자, 신분당선, 정자역, 강남역, 5);

        // then
        지하철_노선_조회_요청(신분당선);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("구간 추가 - 이미 등록되어있는 역을 등록하면 구간을 추가할 수 없다. (400)")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청_withToken(사용자, 신분당선, 강남역, 광교역, 3);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("구간 추가 - 지하철 노선에 등록되지 않은 역을 기준으로 구간을 추가할 수 없다. (400)")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청_withToken(사용자, 신분당선, 정자역, 양재역, 3);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("구간 추가 - 지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_구간_생성_요청_withToken(사용자, 신분당선, 강남역, 양재역, 2);
        지하철_구간_생성_요청_withToken(사용자, 신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청_withToken(사용자, 신분당선, 양재역);

        // then
        assertThat(removeResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("구간 제거 - 지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청_withToken(사용자, 신분당선, 강남역);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }

    public static void 지하철_구간_등록되어_있음(TokenResponse tokenResponse, LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance) {
        지하철_구간_생성_요청_withToken(tokenResponse, lineResponse, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/api/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청_withToken(TokenResponse tokenResponse, LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
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
                .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청_withToken(TokenResponse tokenResponse, LineResponse line, StationResponse stations) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), stations.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_구간_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_구간_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
