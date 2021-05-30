package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_조회_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.util.ExceptionCheck;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse uploadedStation1;
    private StationResponse waitingStation1;
    private StationResponse waitingStation2;
    private StationResponse uploadedStation2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        uploadedStation1 = 지하철역_등록되어_있음("강남역");
        waitingStation1 = 지하철역_등록되어_있음("양재역");
        waitingStation2 = 지하철역_등록되어_있음("정자역");
        uploadedStation2 = 지하철역_등록되어_있음("광교역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", uploadedStation1, uploadedStation2, 10);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, uploadedStation1,
            waitingStation1, 3);

        // then
        지하철_구간_생성됨(response, 신분당선,
            Arrays.asList(uploadedStation1, waitingStation1, uploadedStation2));
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addLineSection2() {
        // when
        지하철_구간_생성_요청(신분당선, uploadedStation1, waitingStation1, 2);
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, waitingStation2,
            uploadedStation1, 5);

        // then
        지하철_구간_생성됨(response, 신분당선,
            Arrays.asList(waitingStation2, uploadedStation1, waitingStation1, uploadedStation2));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, uploadedStation1,
            uploadedStation2, 3);

        // then
        ExceptionCheck.코드_400_응답됨(response);
        ExceptionCheck.에러_문구_확인(response, "BOTH_STATION_ALREADY_REGISTERED_IN_LINE");
    }

    @DisplayName("두 역 모두 노선에 존재하지 않을 때 등록 요청.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, waitingStation2,
            waitingStation1, 3);

        // then
        ExceptionCheck.코드_400_응답됨(response);
        ExceptionCheck.에러_문구_확인(response, "BOTH_STATION_NOT_REGISTERED_IN_LINE");
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        지하철_구간_생성_요청(신분당선, uploadedStation1, waitingStation1, 2);
        지하철_구간_생성_요청(신분당선, waitingStation1, waitingStation2, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, waitingStation1);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, Arrays.asList(uploadedStation1, waitingStation2,
            uploadedStation2));
    }

    @DisplayName("노선에 역이 두 개 밖에 없을 때에는 구간 삭제 불가능.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, uploadedStation1);

        // then
        ExceptionCheck.코드_400_응답됨(removeResponse);
        ExceptionCheck.에러_문구_확인(removeResponse, "ONLY_ONE_SECTION_EXISTS");
    }

    @DisplayName("두 역이 같을 경우")
    @Test
    void FindEmptySection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, uploadedStation1,
            uploadedStation1, 3);
        // then
        ExceptionCheck.코드_400_응답됨(response);
        ExceptionCheck.에러_문구_확인(response, "SAME_STATIONS_IN_SAME_SECTION");
    }

    @DisplayName("유효하지 않은 거리가 온 경우")
    @Test
    void wrongDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, uploadedStation1,
            waitingStation1, 0);
        // then
        ExceptionCheck.코드_400_응답됨(response);
        ExceptionCheck.에러_문구_확인(response, "INVALID_DISTANCE");
    }

    @DisplayName("불가능한 거리가 입력 된 경우")
    @Test
    void impossibleDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, uploadedStation1,
            waitingStation1, 20);
        // then
        ExceptionCheck.코드_400_응답됨(response);
        ExceptionCheck.에러_문구_확인(response, "IMPOSSIBLE_DISTANCE");
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> result, LineResponse lineResponse,
        List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_구간_등록되어_있음(LineResponse lineResponse, StationResponse upStation,
        StationResponse downStation, int distance) {
        지하철_구간_생성_요청(lineResponse, upStation, downStation, distance);
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line,
        StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(),
            distance);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(ExceptionCheck.getDefaultToken())
            .body(sectionRequest)
            .when().post("/api/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response,
        List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line,
        StationResponse station) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(ExceptionCheck.getDefaultToken())
            .when()
            .delete("/api/lines/{lineId}/sections?stationId={stationId}", line.getId(),
                station.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_구간_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> result,
        LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
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
