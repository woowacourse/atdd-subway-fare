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
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionInLineResponse;
import wooteco.subway.line.dto.SectionResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성됨;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static StationResponse 강남역;
    private static StationResponse 광교역;
    private static LineRequest 신분당선;
    private static LineRequest 구신분당선;
    private static SectionInLineResponse 강남에서광교;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        광교역 = 지하철역_등록되어_있음("광교역");

        강남에서광교 = new SectionInLineResponse(강남역, 광교역, 10);

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        구신분당선 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(사용자, 신분당선);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("생성 - 로그인하지 않은 사용자나 유효하지 않은 회원이 요청시 예외를 발생한다.")
    @Test
    void createLineWhenNotValidMember() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(비회원, 신분당선);

        // then
        비회원_요청_실패됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(사용자, 신분당선);

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
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(사용자, lineResponse, 구신분당선);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("수정 - 로그인하지 않은 사용자나 유효하지 않은 회원이 요청시 예외를 발생한다.")
    @Test
    void updateLineWhenNotValidMember() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(비회원, lineResponse, 구신분당선);

        // then
        비회원_요청_실패됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(사용자, lineResponse);

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("제거 - 로그인하지 않은 사용자나 유효하지 않은 회원이 요청시 예외를 발생한다.")
    @Test
    void deleteLineWhenNotValidMember() {
        // given
        LineResponse lineResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(비회원, lineResponse);

        // then
        비회원_요청_실패됨(response);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(사용자, lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(TokenResponse token, LineRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(TokenResponse token, LineResponse response, LineRequest params) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(TokenResponse token, LineResponse lineResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/api/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/api/lines/1");
        LineResponse res = response.as(LineResponse.class);
        assertThat(res.getSections()).usingRecursiveComparison().isEqualTo(Arrays.asList(강남에서광교));
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
        assertThat(resultResponse.getName()).isEqualTo("신분당선");
        assertThat(resultResponse.getColor()).isEqualTo("bg-red-600");

        assertThat(resultResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(강남역, 광교역));
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<LineResponse> createdResponses) {
        List<LineResponse> resultLineIds = response.jsonPath().getList(".", LineResponse.class);

        assertThat(resultLineIds).usingRecursiveComparison().isEqualTo(createdResponses);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
