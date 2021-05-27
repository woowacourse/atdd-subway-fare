package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.subway.member.MemberAcceptanceTest.응답코드_CREATED_확인;
import static wooteco.subway.member.MemberAcceptanceTest.응답코드_확인;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_생성_응답으로부터_StationResponse_추출;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static final String LINE_NAME_1 = "노선이름1";
    private static final String COLOR_1 = "색깔1";
    private static final String LINE_NAME_2 = "노선이름2";
    private static final String COLOR_2 = "색깔2";

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        ExtractableResponse<Response> stationResponse1 = 지하철역_생성_요청("강남역", "create-station");
        응답코드_CREATED_확인(stationResponse1);
        강남역 = 지하철역_생성_응답으로부터_StationResponse_추출(stationResponse1);

        ExtractableResponse<Response> stationResponse2 = 지하철역_생성_요청("광교역", "create-station");
        응답코드_CREATED_확인(stationResponse2);
        광교역 = 지하철역_생성_응답으로부터_StationResponse_추출(stationResponse2);

        lineRequest1 = new LineRequest(LINE_NAME_1, COLOR_1, 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest(LINE_NAME_2, COLOR_2, 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, "create-line");

        // then
        응답코드_CREATED_확인(response);
    }

    @DisplayName("지하철 노선 생성 - 노선 이름: 2자 이상 10자 이하의 한글, 숫자로만 이루어져 있다.(공백 허용 X) - 생성 성공")
    @ParameterizedTest
    @ValueSource(strings = {"하나", "11", "하나2선", "일이삼사오육칠팔구십"})
    void createLineWithValidName(String name) {
        // given
        lineRequest1 = new LineRequest(name, COLOR_1, 강남역.getId(), 광교역.getId(), 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, "create-line");

        // then
        응답코드_CREATED_확인(response);
    }

    @DisplayName("지하철 노선 생성 - 유효하지 않은 노선 이름: 2자 이상 10자 이하의 한글, 숫자로만 이루어져 있다.(공백 허용 X) - 400 에러")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "일", "asdf", "일 ", "하나 선", "하나a선", "하나,선", "하나\t선", "일이삼사오육칠팔구십일"})
    void createLineWithInvalidName(String name) {
        // given
        lineRequest1 = new LineRequest(name, COLOR_1, 강남역.getId(), 광교역.getId(), 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest1, "create-line-fail-invalid-line-name");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다. - 404 에러")
    @Test
    void createLineWithDuplicateName() {
        // given
        LineRequest oldLineRequest = new LineRequest(LINE_NAME_1, COLOR_1, 강남역.getId(), 광교역.getId(), 10);
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성_요청(oldLineRequest, "create-line");
        응답코드_CREATED_확인(createdResponse);

        // when
        LineRequest newLineRequest = new LineRequest(LINE_NAME_1, COLOR_2, 강남역.getId(), 광교역.getId(), 10);
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(newLineRequest, "create-line-fail-name-duplicate");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("기존에 존재하는 지하철 노선 색깔로 지하철 노선을 생성한다. - 404 에러")
    @Test
    void createLineWithDuplicateColor() {
        // given
        LineRequest oldLineRequest = new LineRequest(LINE_NAME_1, COLOR_1, 강남역.getId(), 광교역.getId(), 10);
        ExtractableResponse<Response> createdResponse = 지하철_노선_생성_요청(oldLineRequest, "create-line");
        응답코드_CREATED_확인(createdResponse);

        // when
        LineRequest newLineRequest = new LineRequest(LINE_NAME_2, COLOR_1, 강남역.getId(), 광교역.getId(), 10);
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(newLineRequest, "create-line-fail-color-duplicate");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("상행 종점, 하행 종점이 같은 노선을 생성하는 경우 - 404 에러")
    @Test
    void createLineWithUpStationDownStationSame() {
        // given
        LineRequest request = new LineRequest(LINE_NAME_1, COLOR_1, 강남역.getId(), 강남역.getId(), 10);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(request, "create-line-fail-upStation-downStation-same");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getAllLines() {
        // given
        ExtractableResponse<Response> lineCreateResponse1 = 지하철_노선_생성_요청(lineRequest1, "create-line");
        응답코드_CREATED_확인(lineCreateResponse1);
        LineResponse lineResponse1 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse1);

        ExtractableResponse<Response> lineCreateResponse2 = 지하철_노선_생성_요청(lineRequest2, "create-line");
        응답코드_CREATED_확인(lineCreateResponse2);
        LineResponse lineResponse2 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청("get-all-lines");

        // then
        응답코드_확인(response, HttpStatus.OK);
        지하철_노선_목록_조회시_정확히_모두_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("GET '/api/lines' 로 지하철 노선 목록을 조회한다.")
    @Test
    void getAllLinesApi() {
        // given
        ExtractableResponse<Response> lineCreateResponse1 = 지하철_노선_생성_요청(lineRequest1, "create-line");
        응답코드_CREATED_확인(lineCreateResponse1);
        LineResponse lineResponse1 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse1);

        ExtractableResponse<Response> lineCreateResponse2 = 지하철_노선_생성_요청(lineRequest2, "create-line");
        응답코드_CREATED_확인(lineCreateResponse2);
        LineResponse lineResponse2 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_API_LINES_로_요청();

        // then
        응답코드_확인(response, HttpStatus.OK);
        지하철_노선_목록_조회시_정확히_모두_포함됨(response, Arrays.asList(lineResponse1, lineResponse2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> lineCreateResponse = 지하철_노선_생성_요청(lineRequest1, "create-line");
        응답코드_CREATED_확인(lineCreateResponse);
        LineResponse lineResponse = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse.getId(), "get-line");

        // then
        응답코드_확인(response, HttpStatus.OK);
        지하철_노선_응답_일치_확인(response, lineResponse);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> lineCreateResponse = 지하철_노선_생성_요청(lineRequest1, "create-line");
        응답코드_CREATED_확인(lineCreateResponse);
        LineResponse lineResponse = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse);

        // when
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(lineRequest2.getName(), lineRequest2.getColor());
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse.getId(), lineUpdateRequest, "update-line");

        // then
        응답코드_확인(response, HttpStatus.OK);
    }

    @DisplayName("지하철 노선 수정 - 노선 이름: 2자 이상 10자 이하의 한글, 숫자로만 이루어져 있다.(공백 허용 X) - 예외상황 400 에러")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "일", "asdf", "일 ", "하나 선", "하나a선", "하나,선", "하나\t선", "일이삼사오육칠팔구십일"})
    void updateLineWithInvalidName(String name) {
        // given
        ExtractableResponse<Response> lineCreateResponse = 지하철_노선_생성_요청(lineRequest1, "create-line");
        응답코드_CREATED_확인(lineCreateResponse);
        LineResponse lineResponse = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse);

        // when
        LineUpdateRequest updateRequest = new LineUpdateRequest(name, COLOR_2);
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse.getId(), updateRequest, "update-line-fail-invalid-name");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }


    @DisplayName("지하철 노선 수정 에러 - 이미 존재하는 노선 이름")
    @Test
    void updateLineWithDuplicateName() {
        // given
        ExtractableResponse<Response> lineCreateResponse1 = 지하철_노선_생성_요청(lineRequest1, "create-line");
        응답코드_CREATED_확인(lineCreateResponse1);
        LineResponse lineResponse1 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse1);

        ExtractableResponse<Response> lineCreateResponse2 = 지하철_노선_생성_요청(lineRequest2, "create-line");
        응답코드_CREATED_확인(lineCreateResponse2);
        LineResponse lineResponse2 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse2);

        // when
        LineUpdateRequest updateRequestOfLine2 = new LineUpdateRequest(lineResponse1.getName(), lineResponse2.getColor());
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse2.getId(), updateRequestOfLine2, "update-line-fail-name-duplicate");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지하철 노선 수정 에러 - 이미 존재하는 노선 색깔")
    @Test
    void updateLineWithDuplicateColor() {
        // given
        ExtractableResponse<Response> lineCreateResponse1 = 지하철_노선_생성_요청(lineRequest1, "create-line");
        응답코드_CREATED_확인(lineCreateResponse1);
        LineResponse lineResponse1 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse1);

        ExtractableResponse<Response> lineCreateResponse2 = 지하철_노선_생성_요청(lineRequest2, "create-line");
        응답코드_CREATED_확인(lineCreateResponse2);
        LineResponse lineResponse2 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse2);

        // when
        LineUpdateRequest updateRequestOfLine2 = new LineUpdateRequest(lineResponse2.getName(), lineResponse1.getColor());
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponse2.getId(), updateRequestOfLine2, "update-line-fail-color-duplicate");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> lineCreateResponse1 = 지하철_노선_생성_요청(lineRequest1, "create-line");
        응답코드_CREATED_확인(lineCreateResponse1);
        LineResponse lineResponse1 = 지하철_노선_생성_응답으로부터_LineResponse_추출(lineCreateResponse1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponse1.getId(), "delete-line");

        // then
        응답코드_확인(response, HttpStatus.NO_CONTENT);
    }

    @DisplayName("지하철 노선을 제거 예외 - 없는 노선")
    @Test
    void deleteLineNotExists() {
        // given
        Long lineIdNotExists = 100L;

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineIdNotExists, "delete-line-fail-not-exists");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color, StationResponse upStationResponse, StationResponse downStationResponse, int distance, int extraFare, String docsIdentifier) {
        LineRequest lineCreateRequest = new LineRequest(name, color, upStationResponse.getId(), downStationResponse.getId(), distance, extraFare);
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineCreateRequest)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest)
            .when().post("/lines")
            .then().log().all()
            .extract();
    }

    public static LineResponse 지하철_노선_생성_응답으로부터_LineResponse_추출(ExtractableResponse<Response> response) {
        return response.as(LineResponse.class);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest, "create-line").as(LineResponse.class);
    }

    public static void 지하철_노선_생성_요청_실패(LineRequest params, String documentIdentifier) {
        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .filter(document(documentIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_수정_요청_실패(LineResponse lineResponse, LineUpdateRequest params, String documentIdentifier) {
        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .filter(document(documentIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().put("/lines/" + lineResponse.getId())
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_API_LINES_로_요청() {
        return RestAssured
            .given(spec).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/lines")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{lineId}", id)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long idToUpdate, LineUpdateRequest lineUpdateRequest, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineUpdateRequest)
            .when().put("/lines/" + idToUpdate)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(Long id, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().delete("/lines/" + id)
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

    public static void 지하철_노선_응답_일치_확인(ExtractableResponse<Response> response, LineResponse lineResponse) {
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineResponse.getId());
    }

    public static void 지하철_노선_목록_조회시_정확히_모두_포함됨(ExtractableResponse<Response> getAllResponse, List<LineResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(LineResponse::getId)
            .collect(Collectors.toList());

        List<Long> actualLineIds = getAllResponse.jsonPath().getList(".", LineResponse.class).stream()
            .map(LineResponse::getId)
            .collect(Collectors.toList());

        assertThat(actualLineIds).containsExactlyInAnyOrderElementsOf(expectedLineIds);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
