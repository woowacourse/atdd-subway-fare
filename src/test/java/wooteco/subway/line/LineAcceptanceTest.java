package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
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
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineSectionResponse;
import wooteco.subway.line.dto.SectionResponse;
import wooteco.subway.line.dto.StationTransferResponse;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 광교역;
    private LineRequest 신분당선;
    private LineRequest 구신분당선;
    private LineRequest 경의중앙선;

    private static ExtractableResponse<Response> 구간_정보_조회(long lineId) {
        return RestAssured
            .given().log().all()
            .when().get("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation,
        StationResponse downStation, int fare, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(),
            downStation.getId(), fare, distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all().
                extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{lineId}", response.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response,
        LineRequest params) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/lines/" + response.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse) {
        return RestAssured
            .given().log().all()
            .when().delete("/lines/" + lineResponse.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response,
        LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response,
        List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
            .map(LineResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        역삼역 = 지하철역_등록되어_있음("역삼역");
        광교역 = 지하철역_등록되어_있음("광교역");

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 900, 10);
        구신분당선 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
        경의중앙선 = new LineRequest("경의중앙선", "bg-red-600", 강남역.getId(), 역삼역.getId(), 20);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(신분당선);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(구신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, 구신분당선);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("구간 정보 조회 기능")
    @Test
    void findSections() {
        // given
        지하철_노선_생성_요청(신분당선);
        지하철_노선_생성_요청(구신분당선);
        지하철_노선_생성_요청(경의중앙선);

        List<TransferLineResponse> 강남역_환승노선들 = Arrays.asList(
            new TransferLineResponse(2L, 구신분당선.getName(), 구신분당선.getColor()),
            new TransferLineResponse(3L, 경의중앙선.getName(), 경의중앙선.getColor())
        );
        List<TransferLineResponse> 광교역_환승노선들 = Arrays.asList(
            new TransferLineResponse(2L, 구신분당선.getName(), 구신분당선.getColor())
        );

        List<StationTransferResponse> stationTransferResponses = Arrays.asList(
            new StationTransferResponse(강남역.getId(), 강남역.getName(), 강남역_환승노선들),
            new StationTransferResponse(광교역.getId(), 광교역.getName(), 광교역_환승노선들));

        List<SectionResponse> sectionResponses = Arrays.asList(
            SectionResponse.of(
                new Section(new Station(강남역.getId(), 강남역.getName()),
                    new Station(광교역.getId(), 광교역.getName()), 10)
            )
        );

        // when
        ExtractableResponse<Response> response = 구간_정보_조회(1L);
        LineSectionResponse lineSectionResponse = response.as(LineSectionResponse.class);

        // then
        assertThat(lineSectionResponse.getId()).isEqualTo(1L);
        assertThat(lineSectionResponse.getName()).isEqualTo(신분당선.getName());
        assertThat(lineSectionResponse.getColor()).isEqualTo(신분당선.getColor());
        assertThat(lineSectionResponse.getStations()).usingRecursiveComparison()
            .isEqualTo(stationTransferResponses);
        assertThat(lineSectionResponse.getSections()).usingRecursiveComparison()
            .isEqualTo(sectionResponses);
    }
}
