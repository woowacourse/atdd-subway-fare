package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.회원가입_후_로그인;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;

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
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    private TokenResponse tokenResponse;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenResponse = 회원가입_후_로그인();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역 , tokenResponse);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역 , tokenResponse);

        // then
        지하철역_생성_실패됨(response);
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

    @DisplayName("노선에 등록된 지하철역을 제거한다.")
    @Test
    void deleteStationInLine() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역, tokenResponse);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역, tokenResponse);
        LineRequest lineRequest1 = new LineRequest("로키네선", "bg-pinkpink-600",
                stationResponse1.getId(), stationResponse2.getId(), 10);
        지하철_노선_등록되어_있음(lineRequest1, tokenResponse);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse1, tokenResponse);

        // then
        지하철역_삭제_실패됨(response);
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

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse, TokenResponse tokenResponse) {
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

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
}
