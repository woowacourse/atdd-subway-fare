package wooteco.subway.station;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.auth.AuthAcceptanceTest.*;
import static wooteco.subway.line.LineAcceptanceTest.*;

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

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";
    private static String accessToken;

    public static StationResponse 지하철역_등록되어_있음(String name, String accessToken) {
        return 지하철역_생성_요청(accessToken, name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String accessToken, String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
            .given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when().post("/api/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/api/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().delete("/api/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> response, List<StationResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);
        accessToken = tokenResponse.getAccessToken();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(accessToken, 강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("이미 존재하는 지하철역을 생성한다.")
    @Test
    void createDuplicatedStation() {
        // when
        지하철역_생성_요청(accessToken, 강남역);
        ExtractableResponse<Response> response = 지하철역_생성_요청(accessToken, "강남역");

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("DUPLICATED_STATION_NAME");
    }

    @DisplayName("유효하지 않은 토큰으로 지하철역을 생성한다.")
    @Test
    void createStationWithoutToken() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청("accessToken", 강남역);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("INVALID_TOKEN");
    }

    @DisplayName("옳지 않은 이름으로 지하철역을 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "강남역!", "  "})
    void createInvalidStation(String name) {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(accessToken, name);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("INVALID_NAME");
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역, accessToken);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(accessToken, 강남역);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역, accessToken);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역, accessToken);

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
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역, accessToken);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("존재하지 않는 지하철역을 제거한다.")
    @Test
    void deleteNotExistStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(new StationResponse(55L, "없는역"));

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("NO_SUCH_STATION");
    }

    @DisplayName("노선에 등록된 지하철역을 제거한다.")
    @Test
    void deleteAlreadyInLineStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역, accessToken);
        StationResponse stationResponse1 = 지하철역_등록되어_있음(역삼역, accessToken);
        LineRequest lineRequest = new LineRequest("name", "color", stationResponse.getId(), stationResponse1.getId(),
            100);
        지하철_노선_생성_요청(accessToken, lineRequest);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse);

        // then
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getError()).isEqualTo("STATION_ALREADY_REGISTERED_IN_LINE");
    }
}
