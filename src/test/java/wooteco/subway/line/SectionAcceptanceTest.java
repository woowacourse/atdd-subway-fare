package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.AuthAcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.line.exception.SectionException;
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

    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        tokenResponse = AuthAcceptanceTest.회원가입_토큰가져오기();

        강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        양재역 = 지하철역_등록되어_있음("양재역", tokenResponse);
        정자역 = 지하철역_등록되어_있음("정자역", tokenResponse);
        광교역 = 지하철역_등록되어_있음("광교역", tokenResponse);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10, tokenResponse);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 양재역, 3, tokenResponse);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2, tokenResponse);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 강남역, 5, tokenResponse);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 광교역, 3, tokenResponse);

        // then
        에러_발생함(response, SectionException.INVALID_SECTION_INFO_EXCEPTION);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 양재역, 3, tokenResponse);

        // then
        에러_발생함(response, SectionException.INVALID_SECTION_INFO_EXCEPTION);
    }

    @DisplayName("삽입되는 구간이 기존 구간보다 길이가 길거나 같으면 안된다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    void addLineSectionWithIllegalDistance(int distance) {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 양재역, distance, tokenResponse);

        // then
        에러_발생함(response, SectionException.INVALID_SECTION_INFO_EXCEPTION);
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2, tokenResponse);
        지하철_구간_생성_요청(신분당선, 양재역, 정자역, 2, tokenResponse);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역, tokenResponse);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, Arrays.asList(강남역, 정자역, 광교역), tokenResponse);
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역, tokenResponse);

        // then
        에러_발생함(removeResponse, SectionException.ILLEGAL_SECTION_DELETE_EXCEPTION);
    }

    public static void 지하철_구간_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance, TokenResponse tokenResponse) {
        지하철_구간_생성_요청(lineResponse, upStation, downStation, distance, tokenResponse);
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance, TokenResponse tokenResponse) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
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

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_구간_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse, tokenResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses, TokenResponse tokenResponse) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse, tokenResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }
}
