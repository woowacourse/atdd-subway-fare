package wooteco.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.StationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    private static TokenResponse tokenResponse;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenResponse = AuthAcceptanceTest.회원가입_토큰가져오기();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        에러_발생함(response, StationException.DUPLICATED_STATION_NAME_EXCEPTION);
    }

    @DisplayName("역을 생성 할 때 이름은 2글자 이상 20글자 이하가 되어야한다. 공백이 불가하다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "a", "20글자이상의지하철역이름은생성을할수가없습니다.", "공백도 불가해요", " 앞뒤 공백 불가 ", "NotEnglish", "!!#$%^"})
    void createStationNameLength(String name) {
        ExtractableResponse<Response> response = 지하철역_생성_요청(name);

        에러_발생함(response, StationException.INVALID_STATION_NAME_LENGTH_EXCEPTION);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void updateStation() {
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        ExtractableResponse<Response> response = 지하철역_수정_요청(new StationRequest("잠실역"), stationResponse.getId());

        지하철역_이름_수정됨(response);
    }

    @DisplayName("지하철역을 수정할 때 중복된 이름을 사용할 수 없다.")
    @Test
    void updateStationWithDuplicate() {
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);
        지하철역_등록되어_있음(역삼역);

        ExtractableResponse<Response> response = 지하철역_수정_요청(new StationRequest("역삼역"), stationResponse.getId());

        에러_발생함(response, StationException.DUPLICATED_STATION_NAME_EXCEPTION);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("존재하지 않는 역을 제거, 수정할 경우")
    @Test
    void notExistStationDeleteAndUpdate() {
        ExtractableResponse<Response> deleteResponse = 지하철역_제거_요청(new StationResponse(0L, "없는역"));

        에러_발생함(deleteResponse, StationException.NOT_FOUND_STATION_EXCEPTION);

        ExtractableResponse<Response> updateResponse = 지하철역_수정_요청(new StationRequest("없는역"), 1L);

        에러_발생함(updateResponse, StationException.NOT_FOUND_STATION_EXCEPTION);
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
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

    public static ExtractableResponse<Response> 지하철역_수정_요청(StationRequest stationRequest, Long id) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/stations/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse) {
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

    private void 지하철역_이름_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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
}
