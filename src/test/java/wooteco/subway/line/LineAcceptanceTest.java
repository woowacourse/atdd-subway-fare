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
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LinesResponse;
import wooteco.subway.line.dto.SectionResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_생성_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 역삼역;
    private StationResponse 송파나루역;
    private StationResponse 석촌역;
    private StationResponse 잠실역;

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        광교역 = 지하철역_등록되어_있음("광교역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 900, 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 500, 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성됨(response);
        지하철_노선이_제대로_등록됨(response.as(LineResponse.class), lineRequest1);
    }

    private void 지하철_노선이_제대로_등록됨(LineResponse response, LineRequest lineRequest) {
        assertThat(response.getName()).isEqualTo(lineRequest.getName());
        assertThat(response.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(response.getExtraFare()).isEqualTo(lineRequest.getExtraFare());
    }

    @DisplayName("존재하는 노선을 생성하는 경우 예외 처리한다.")
    @Test
    void cannotCreateExistLine() {
        // given
        지하철_노선_생성_요청(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
        assertThat((String) response.jsonPath().get("message")).isEqualTo("중복된 지하철 노선입니다.");
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성할 경우 예외처리한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1);

        // then
        지하철_노선_생성_실패됨(response);
        assertThat((String) response.jsonPath().get("message")).isEqualTo("중복된 지하철 노선입니다.");
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

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
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);

        // then
        지하철_노선_응답됨(response, lineResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2);

        // then
        지하철_노선_수정됨(response, lineRequest2);
    }

    @DisplayName("이미 존재하는 노선의 이름으로 수정할 경우 예외 처리한다.")
    @Test
    void updateWhenExistLineName() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2);

        // then
        지하철_노선_생성_실패됨(response);
        assertThat((String) response.jsonPath().get("message")).isEqualTo("중복된 지하철 노선입니다.");
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 제거할 경우 예외 처리한다.")
    @Test
    void cannotDeleteWhenLineNotExist() {
        // given
        LineResponse lineResponse = new LineResponse(11L, "존재하지 않는 지하철역", "color", Collections.emptyList());

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse);

        // then
        지하철_노선_생성_실패됨(response);
        assertThat((String) response.jsonPath().get("message")).isEqualTo("존재하지 않는 노선입니다.");
    }

    @DisplayName("구간이 순서대로 들어오지 않아도 정렬된 지하철 전체 노선이 조회된다.")
    @Test
    void getAllLines() {
        // given
        역삼역 = 지하철역_등록되어_있음("역삼역");
        송파나루역 = 지하철역_등록되어_있음("송파나루역");
        석촌역 = 지하철역_등록되어_있음("석촌역");
        잠실역 = 지하철역_등록되어_있음("잠실역");

        LineRequest lineRequest3 = new LineRequest("2호선", "bg-blue-600", 150, 석촌역.getId(), 잠실역.getId(), 3);

        LineResponse 신분당선 = 지하철_노선_등록되어_있음(lineRequest1);
        LineResponse 이호선 = 지하철_노선_등록되어_있음(lineRequest3);
        지하철_구간_생성_요청(신분당선, 역삼역, 강남역, 7);
        지하철_구간_생성_요청(이호선, 송파나루역, 석촌역, 5);

        // when
        ExtractableResponse<Response> response = 지하철_노선_전체_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        List<LinesResponse> linesResponses = response.jsonPath().getList(".", LinesResponse.class);
        지하철_전체_노선_정보가_제대로_등록됨(linesResponses.get(0), lineRequest1);
        지하철_전체_노선_정보가_제대로_등록됨(linesResponses.get(1), lineRequest3);

        assertThat(linesResponses.get(0).getSections()).usingRecursiveComparison().isEqualTo(
                Arrays.asList(
                        new SectionResponse(역삼역, 강남역, 7),
                        new SectionResponse(강남역, 광교역, 10)));
        assertThat(linesResponses.get(1).getSections()).usingRecursiveComparison().isEqualTo(
                Arrays.asList(
                        new SectionResponse(송파나루역, 석촌역, 5),
                        new SectionResponse(석촌역, 잠실역, 3)));
    }

    private void 지하철_전체_노선_정보가_제대로_등록됨(LinesResponse response, LineRequest lineRequest) {
        assertThat(response.getName()).isEqualTo(lineRequest.getName());
        assertThat(response.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(response.getExtraFare()).isEqualTo(lineRequest.getExtraFare());
    }

    private ExtractableResponse<Response> 지하철_노선_전체_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/map")
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        return 지하철_노선_등록되어_있음_추가요금_포함(name, color, 0, upStation, downStation, distance);
    }

    public static LineResponse 지하철_노선_등록되어_있음_추가요금_포함(String name, String color, int extraFare, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, extraFare, upStation.getId(), downStation.getId(), distance);
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

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineRequest params) {
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

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponse lineResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response, LineRequest lineRequest) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getName()).isEqualTo(lineRequest.getName());
        assertThat(resultResponse.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(resultResponse.getExtraFare()).usingRecursiveComparison().isEqualTo(lineRequest.getExtraFare());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
