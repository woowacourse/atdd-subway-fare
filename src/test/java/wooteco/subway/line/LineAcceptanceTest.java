package wooteco.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.DocsIdentifier;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.MapResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.DocsIdentifier.*;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params, String identifier) {
        return given(identifier)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return given(LINES_GET)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_전체_노선도_조회_요청() {
        return given(LINES_GET_MAP)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/map")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return given(LINES_GET_BY_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineRequest params, String identifier) {

        return given(identifier)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse, String identifier) {
        return given(identifier)
                .when().delete("/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, Long extraFare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest, LINES_POST_SUCCESS).as(LineResponse.class);
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

    public static void 지하철_전체_노선도_포함됨(ExtractableResponse<Response> response, List<MapResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철_전체_노선도_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        광교역 = 지하철역_등록되어_있음("광교역");

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, LINES_POST_SUCCESS);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, LINES_POST_FAIL_DUPLICATED);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("노선이름은 2자 이상 10자 이하의 한글 (숫자 포함. 공백 허용 X)")
    @Test
    void createErrorNameLine() throws Exception {
        //given
        String errorName = "공백 선";
        LineRequest errorLineRequest = new LineRequest(errorName, "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        //when
        ExtractableResponse<Response> extractableResponse = 지하철_노선_생성_요청(errorLineRequest, LINES_POST_FAIL_NAME_BLANK);

        //then
        지하철_노선_생성_실패됨(extractableResponse);

        //given
        errorName = "선";
        errorLineRequest = new LineRequest(errorName, "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        //when
        extractableResponse = 지하철_노선_생성_요청(errorLineRequest, DocsIdentifier.LINES_POST_FAIL_NAME_LENGTH);

        //then
        지하철_노선_생성_실패됨(extractableResponse);

        //given
        errorName = "진짜진짜진짜기다란이상한노션아닌노선";
        errorLineRequest = new LineRequest(errorName, "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        //when
        extractableResponse = 지하철_노선_생성_요청(errorLineRequest, LINES_POST_FAIL_NAME_LENGTH);

        //then
        지하철_노선_생성_실패됨(extractableResponse);
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

    @DisplayName("지하철 전체 노선도를 조회한다.")
    @Test
    void getLineMap() {
        // given
        LineResponse lineResponse1 = 지하철_노선_등록되어_있음(lineRequest1);
        LineResponse lineResponse2 = 지하철_노선_등록되어_있음(lineRequest2);

        MapResponse expectedMapResponse1 = new MapResponse(lineResponse1.getId(), lineResponse1.getName(),
                lineResponse1.getColor(), lineResponse1.getExtraFare(), lineResponse1.getSections());

        MapResponse expectedMapResponse2 = new MapResponse(lineResponse2.getId(), lineResponse2.getName(),
                lineResponse2.getColor(), lineResponse2.getExtraFare(), lineResponse2.getSections());
        // when
        ExtractableResponse<Response> response = 지하철_전체_노선도_조회_요청();

        // then
        지하철_전체_노선도_응답됨(response);
        지하철_전체_노선도_포함됨(response, Arrays.asList(expectedMapResponse1, expectedMapResponse2));
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
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest2, LINES_PUT_SUCCESS);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("중복된 노선으로 수정한다.")
    @Test
    void updateDuplicatedLine() throws Exception {
        //given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        //when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest1, LINES_PUT_FAIL_DUPLICATED);

        //then
        지하철_노선_수정_실패(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정한다.")
    @Test
    void updateNotExistLine() throws Exception {
        //given
        LineResponse lineResponse = new LineResponse(Long.MAX_VALUE, null, null, null, null);

        //when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse, lineRequest1, LINES_PUT_FAIL_NOT_EXIST);

        //then
        지하철_노선_수정_실패(response);
    }

    @DisplayName("지하철 노선 수정 시 이름 유효성을 검사한다. 노선이름은 2자 이상 10자 이하의 한글 (숫자 포함. 공백 허용 X)")
    @Test
    void validateNameWhenLineUpdated() throws Exception {
        //given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);


        //given
        String errorName = "공백 선";
        LineRequest errorLineRequest = new LineRequest(errorName, "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        //when
        ExtractableResponse<Response> extractableResponse = 지하철_노선_수정_요청(lineResponse, errorLineRequest, LINES_PUT_FAIL_NAME_BLANK);

        //then
        지하철_노선_수정_실패(extractableResponse);

        //given
        errorName = "선";
        errorLineRequest = new LineRequest(errorName, "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        //when
        extractableResponse = 지하철_노선_수정_요청(lineResponse, errorLineRequest, LINES_PUT_FAIL_NAME_LENGTH);

        //then
        지하철_노선_수정_실패(extractableResponse);

        //given
        errorName = "진짜진짜진짜기다란이상한노션아닌노선";
        errorLineRequest = new LineRequest(errorName, "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        //when
        extractableResponse = 지하철_노선_수정_요청(lineResponse, errorLineRequest, LINES_PUT_FAIL_NAME_LENGTH);

        //then
        지하철_노선_수정_실패(extractableResponse);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse, LINES_DELETE_SUCCESS);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("존재하지 않는 지하철 노선을 삭제한다.")
    @Test
    void deleteNotExistLine() throws Exception {
        //given
        LineResponse lineResponse = new LineResponse(Long.MAX_VALUE, null, null, null, null);

        //when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse, LINES_DELETE_FAIL);

        //then
        지하철_노선_삭제_실패(response);
    }
}
