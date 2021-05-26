package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.AuthAcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.LineAcceptanceTest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.SubwayStationException;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    private TokenResponse tokenResponse;

    @BeforeEach
    void beforeSetup() {
        tokenResponse = AuthAcceptanceTest.회원가입_토큰가져오기();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역, tokenResponse);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역, tokenResponse);

        // then
        에러가_발생한다(response, SubwayStationException.DUPLICATE_STATION_EXCEPTION);
    }

    @DisplayName("2글자이상 20글자이하 한글,숫자가 아닌 이름으로 지하철역을 생성하면 에러가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "일", "abc", "20글자이상의지하철역이름은생성을할수가없습니다", "공백이 들어가면 안됩니다",
        "특수문자안됨!!"})
    void createStationWithDuplicateName(String value) {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(value, tokenResponse);

        // then
        에러가_발생한다(response, SubwayStationException.INVALID_STATION_NAME_EXCEPTION);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역, tokenResponse);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청(tokenResponse);

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철 역을 수정한다.")
    @Test
    void updateStation() {
        StationResponse stationResponse = 지하철역_등록되어_있음(역삼역, tokenResponse);
        StationRequest stationRequest = new StationRequest(강남역);

        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse, stationRequest,
            tokenResponse);
        지하철역_수정됨(response);
    }

    @DisplayName("존재하지 않는 지하철 역을 수정하면 에러가 발생한다.")
    @Test
    void updateStationWithNotExistStation() {
        StationResponse stationResponse = new StationResponse(1L, 역삼역);
        StationRequest stationRequest = new StationRequest(강남역);

        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse, stationRequest,
            tokenResponse);
        에러가_발생한다(response, SubwayStationException.NOT_EXIST_STATION_EXCEPTION);
    }

    @DisplayName("이미 존재하는 역의 이름으로 수정하면 에러가 발생한다.")
    @Test
    void updateStationWithDuplicateName() {
        지하철역_등록되어_있음(강남역, tokenResponse);
        StationResponse stationResponse = 지하철역_등록되어_있음(역삼역, tokenResponse);

        StationRequest stationRequest = new StationRequest(강남역);

        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse, stationRequest,
            tokenResponse);
        에러가_발생한다(response, SubwayStationException.DUPLICATE_STATION_EXCEPTION);
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

    @DisplayName("존재하지 않는 지하철역을 제거하면 에러가 발생한다")
    @Test
    void deleteStationWithNotExistStation() {
        // given
        StationResponse stationResponse = new StationResponse(1L, 역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse, tokenResponse);

        // then
        에러가_발생한다(response, SubwayStationException.NOT_EXIST_STATION_EXCEPTION);
    }

    @DisplayName("노선에 포함된 지하철역을 제거하면 에러가 발생한다.")
    @Test
    void deleteStationWithUseStation() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역, tokenResponse);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역, tokenResponse);

        LineAcceptanceTest.지하철_노선_등록되어_있음("2호선", "녹색", stationResponse1, stationResponse2, 10, tokenResponse);
        
        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse1, tokenResponse);

        // then
        에러가_발생한다(response, SubwayStationException.DELETE_USE_STATION_EXCEPTION);
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static StationResponse 지하철역_등록되어_있음(String name, TokenResponse tokenResponse) {
        return 지하철역_생성_요청(name, tokenResponse).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name,
        TokenResponse tokenResponse) {
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

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
            .given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse,
        TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .when().delete("/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response,
        List<StationResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> 지하철역_수정_요청(StationResponse stationResponse,
        StationRequest StationRequest, TokenResponse tokenResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(StationRequest)
            .when().put("/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    private void 지하철역_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
