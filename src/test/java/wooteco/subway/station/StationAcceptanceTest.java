package wooteco.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationNameRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;

@DisplayName("지하철역 관련 기능")
@Transactional
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(성인_사용자, 강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("생성 - 로그인하지 않은 사용자나 유효하지 않은 회원이 요청시 예외를 발생한다.")
    @Test
    void createStationWhenNotValidMember() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(비회원, 강남역);

        // then
        비회원_요청_실패됨(response);
    }

    @DisplayName("생성 - 중복된 지하철 역이름으로 생성 요청하는 경우 예외를 던진다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(성인_사용자, 강남역);

        // then
        지하철역_생성_실패됨(response);
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
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역 이름을 수정한다.")
    @Test
    void testChangeStation() {
        // given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청(성인_사용자, 역삼역);
        ExtractableResponse<Response> 역삼응답 = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(역삼응답, Arrays.asList(new StationResponse(1L, 역삼역)));
    }

    @DisplayName("수정 - 로그인하지 않은 사용자나 유효하지 않은 회원이 요청시 예외를 발생한다.")
    @Test
    void testChangeStationWhenNotExistsMember() {
        // given
        지하철역_등록되어_있음(강남역);
        지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청(비회원, 역삼역);

        // then
        비회원_요청_실패됨(response);
    }

    @DisplayName("수정 - 기존에 존재하는 지하철역 이름으로 요청 시 예외를 던진다.")
    @Test
    void testChangeStationWhenAlreadyExistsName() {
        // given
        지하철역_등록되어_있음(강남역);
        지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청(성인_사용자, 역삼역);

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(성인_사용자, stationResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("제거 - 로그인하지 않은 사용자나 유효하지 않은 회원이 요청시 예외를 발생한다.")
    @Test
    void deleteStationWhenNotValidMember() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when

        ExtractableResponse<Response> response = 지하철역_제거_요청(비회원, stationResponse);

        // then
        비회원_요청_실패됨(response);
    }



    @DisplayName("역 제거 - 구간에 존재하는 지하철 역일 경우 삭제 불가")
    @Test
    void removeStationWhenAlreadyRegisteredOnSection() {
        // given
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역");
        StationResponse 광교역 = 지하철역_등록되어_있음("광교역");
        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(성인_사용자, 광교역);

        // then
        assertThat(response.as(ExceptionResponse.class).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철역_수정_요청(TokenResponse token, String name) {
        StationRequest request = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/stations/1")
                .then().log().all()
                .extract();
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(성인_사용자, name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(TokenResponse token, String name) {
        StationNameRequest stationRequest = new StationNameRequest(name);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/api/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(TokenResponse token, StationResponse stationResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/api/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        StationResponse stationRes = response.as(StationResponse.class);
        assertThat(stationRes.getId()).isEqualTo(1);
        assertThat(stationRes.getName()).isEqualTo("강남역");
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
}
