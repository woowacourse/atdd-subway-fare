package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_생성_요청;
import static wooteco.subway.member.MemberAcceptanceTest.AGE;
import static wooteco.subway.member.MemberAcceptanceTest.EMAIL;
import static wooteco.subway.member.MemberAcceptanceTest.PASSWORD;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationTransferResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";
    private TokenResponse tokenResponse;

    @BeforeEach
    void init() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역, tokenResponse);

        // then
        지하철역_생성됨(response, tokenResponse);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicatedException() {
        //given
        지하철역_등록되어_있음(강남역, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역, tokenResponse);

        // then
        지하철역_생성_실패됨_CONFLICT(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역, tokenResponse);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse, tokenResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("존재하지 않는 ID로 지하철역을 지울 수 없다.")
    @Test
    void deleteStationWithNotFoundException() {
        StationResponse stationResponse = new StationResponse(1L, "강남역");

        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse, tokenResponse);

        지하철역_삭제_실패됨_NOT_FOUND(response);
    }

    @DisplayName("노선에 등록된 지하철역을 삭제하려고 하면 실패한다.")
    @Test
    void deleteStationWithInvalidException() {
        // given
        StationResponse upStation = 지하철역_등록되어_있음(강남역, tokenResponse);
        StationResponse downStation = 지하철역_등록되어_있음(역삼역, tokenResponse);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", upStation.getId(), downStation.getId(), 10);
        지하철_노선_생성_요청(lineRequest, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(upStation, tokenResponse);

        // then
        지하철역_삭제_실패됨_BAD_REQUEST(response);
    }

    @DisplayName("입력값이 빈 값, null, 특수 문자, 영어, 글자수는 2자 미만이거나 10자 초과면 안 된다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"썬", "가나다라마바사아자차카타파하, %, )))"})
    void createStationFailWithInvalidException(String name) {
        ExtractableResponse<Response> response = 지하철역_생성_요청(name, tokenResponse);

        지하철역_생성_실패됨_BAD_REQUEST(response);
    }

    @DisplayName("환승 정보를 포함한 모든 지하철역을 조회한다.")
    @Test
    void showStationsWithTransfer() {
        // given
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역", tokenResponse);
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역", tokenResponse);
        StationResponse 정자역 = 지하철역_등록되어_있음("정자역", tokenResponse);
        StationResponse 광교역 = 지하철역_등록되어_있음("광교역", tokenResponse);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10, tokenResponse);
        LineResponse 이호선 = 지하철_노선_등록되어_있음("2호선", "bg-red-601", 강남역, 양재역, 10, tokenResponse);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음("3호선", "bg-red-602", 강남역, 정자역, 10, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_환승_포함_목록_조회_요청();

        // then
        지하철역_환승_포함_목록_응답됨(response);
        List<List<String>> expected = Arrays
            .asList(Arrays.asList("신분당선", "2호선", "3호선"),
                Collections.singletonList("2호선"),
                Collections.singletonList("3호선"),
                Collections.singletonList("신분당선"));
        지하철역_환승_포함_목록_포함됨(response, expected);
    }

    public static StationResponse 지하철역_등록되어_있음(String name, TokenResponse tokenResponse) {
        return 지하철역_생성_요청(name, tokenResponse).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, TokenResponse tokenResponse) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_환승_포함_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/stations/transfer")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse, TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete("/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response, TokenResponse tokenResponse) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨_BAD_REQUEST(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_생성_실패됨_CONFLICT(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_환승_포함_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public void 지하철역_삭제_실패됨_NOT_FOUND(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public void 지하철역_삭제_실패됨_BAD_REQUEST(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<StationResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철역_환승_포함_목록_포함됨(ExtractableResponse<Response> response, List<List<String>> lines) {
        List<List<String>> stationTransferResponses = response.jsonPath()
            .getList(".", StationTransferResponse.class).stream()
            .map(StationTransferResponse::getTransfer)
            .collect(Collectors.toList());

        assertThat(stationTransferResponses).containsAll(lines);
    }
}
