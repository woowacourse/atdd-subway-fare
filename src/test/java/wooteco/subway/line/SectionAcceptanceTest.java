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
import wooteco.subway.exception.dto.ExceptionResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.member.MemberAcceptanceTest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.*;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private static TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        tokenResponse = MemberAcceptanceTest.회원_로그인된_상태();
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

    @DisplayName("유효하지 않은 토큰으로 지하철 구간을 등록한다.")
    @Test
    void addLineSectionWithInvalidToken() {
        // when
        ExtractableResponse<Response> response = 유효하지_않은_토큰으로_지하철_구간_생성_요청(신분당선, 강남역, 양재역, 3, tokenResponse);

        // then
        잘못된_토큰으로_요청을_보냄(response);
    }

    @DisplayName("존재하지 않는 지하철 역으로 지하철 구간을 등록한다.")
    @Test
    void addLineSectionWithNotExistsStation() {
        // when
        ExtractableResponse<Response> response = 존재하지_않는_지하철역으로_지하철_구간_생성_요청(신분당선, 3, tokenResponse);

        // then
        지하철역이_존재하지_않음(response);
    }

    @DisplayName("유효하지 않은 값으로 지하철 구간을 등록한다.")
    @Test
    void addLineSectionWithInvalidValue() {
        //given
        StationResponse 상행역_Null = new StationResponse(null, 강남역.getName());
        StationResponse 상행역_음수 = new StationResponse(-1L, 강남역.getName());
        StationResponse 하행역_Null = new StationResponse(null, 양재역.getName());
        StationResponse 하행역_음수 = new StationResponse(-1L, 양재역.getName());

        // when
        ExtractableResponse<Response> 상행역_Null_응답 = 지하철_구간_생성_요청(신분당선, 상행역_Null, 양재역, 3, tokenResponse);
        ExtractableResponse<Response> 상행역_음수_응답 = 지하철_구간_생성_요청(신분당선, 상행역_음수, 양재역, 3, tokenResponse);
        ExtractableResponse<Response> 하행역_Null_응답 = 지하철_구간_생성_요청(신분당선, 강남역, 하행역_Null, 3, tokenResponse);
        ExtractableResponse<Response> 하행역_음수_응답 = 지하철_구간_생성_요청(신분당선, 강남역, 하행역_음수, 3, tokenResponse);
        ExtractableResponse<Response> 유효하지_않은_거리_응답 = 지하철_구간_생성_요청(신분당선, 강남역, 양재역, 0, tokenResponse);

        // then
        잘못된_입력값으로_요청을_보냄(상행역_Null_응답);
        지하철_역ID_음수요청됨(상행역_음수_응답);
        잘못된_입력값으로_요청을_보냄(하행역_Null_응답);
        지하철_역ID_음수요청됨(하행역_음수_응답);
        유효하지_않은_거리값_요청됨(유효하지_않은_거리_응답);
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
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 양재역, 3, tokenResponse);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("유효하지 않은 거리로 지하철 노선을 등록한다.")
    @Test
    void addLineSectionWithInvalidDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 광교역, 5, tokenResponse);

        // then
        지하철_구간_등록_실패됨(response);
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
        지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("유효하지 않은 토큰으로 지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSectionWithInvalidToken() {
        // given
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2, tokenResponse);
        지하철_구간_생성_요청(신분당선, 양재역, 정자역, 2, tokenResponse);

        // when
        ExtractableResponse<Response> removeResponse = 유효하지_않은_토큰으로_지하철_노선에_지하철역_제외_요청(신분당선, 양재역, tokenResponse);

        // then
        잘못된_토큰으로_요청을_보냄(removeResponse);
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

    public static void 지하철_구간_등록되어_있음(LineResponse lineResponse, StationResponse upStation, StationResponse downStation, int distance, TokenResponse tokenResponse) {
        지하철_구간_생성_요청(lineResponse, upStation, downStation, distance, tokenResponse);
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

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance, TokenResponse tokenResponse) {
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

    public static ExtractableResponse<Response> 존재하지_않는_지하철역으로_지하철_구간_생성_요청(LineResponse line, int distance, TokenResponse tokenResponse) {
        SectionRequest sectionRequest = new SectionRequest(99999998L, 9999999L, distance);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/api/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 유효하지_않은_토큰으로_지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance, TokenResponse tokenResponse) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "유효하지 않은 토큰")
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
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 유효하지_않은_토큰으로_지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station, TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken() + "유효하지 않은 토큰")
                .when().delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
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
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("유효하지 않는 요청 값입니다");
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역이_존재하지_않음(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo("존재하지 않는 지하철 역입니다");
    }
}
