package wooteco.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.ErrorResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.dto.StationWithTransferResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_생성_요청;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";
    private static final String 을지로3가역 = "을지로3가역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);
        ExtractableResponse<Response> response2 = 지하철역_생성_요청(을지로3가역);

        // then
        지하철역_생성됨(response);
        지하철역_생성됨(response2);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_이름이_중복되어_생성_실패됨(response);
    }

    @DisplayName("지하철 역 이름은 2자 이상 한글/숫자만 가능하며, 공백은 허용하지 않는다")
    @Test
    void createStationWithInvalidName() {
        //given
        String invalidCase1 = "역";
        String invalidCase2 = "station";
        String invalidCase3 = "       ";
        String invalidCase4 = "$#*^";

        // when
        ExtractableResponse<Response> response1 = 지하철역_생성_요청(invalidCase1);
        ExtractableResponse<Response> response2 = 지하철역_생성_요청(invalidCase2);
        ExtractableResponse<Response> response3 = 지하철역_생성_요청(invalidCase3);
        ExtractableResponse<Response> response4 = 지하철역_생성_요청(invalidCase4);

        // then
        지하철역_생성_실패됨(response1);
        지하철역_생성_실패됨(response2);
        지하철역_생성_실패됨(response3);
        지하철역_생성_실패됨(response4);
    }

    @DisplayName("환승 정보가 담긴 지하철역 목록을 조회한다.")
    @Test
    void getStationsWithTransferInfo() {
        // given
        StationResponse 일호선역과이호선역 = 지하철역_등록되어_있음("일호선역과이호선역");
        StationResponse 일호션역 = 지하철역_등록되어_있음("일호선역");
        StationResponse 이호선역 = 지하철역_등록되어_있음("이호선역");
        StationResponse 노선에등록안됨역 = 지하철역_등록되어_있음("노선에등록안됨역");

        LineRequest 일호선 = new LineRequest("1호선", "bg-red-600", 일호선역과이호선역.getId(), 일호션역.getId(), 10);
        지하철_노선_생성_요청(일호선);

        LineRequest 이호선 = new LineRequest("2호선", "bg-blue-600", 일호선역과이호선역.getId(), 이호선역.getId(), 10);
        지하철_노선_생성_요청(이호선);

        StationWithTransferResponse 일호선역과이호선역환승정보 =
                new StationWithTransferResponse(일호선역과이호선역.getId(), 일호선역과이호선역.getName(), Arrays.asList("1호선", "2호선"));

        StationWithTransferResponse 일호선역환승정보 =
                new StationWithTransferResponse(일호션역.getId(), 일호션역.getName(), Arrays.asList("1호선"));

        StationWithTransferResponse 이호선역환승정보 =
                new StationWithTransferResponse(이호선역.getId(), 이호선역.getName(), Arrays.asList("2호선"));

        // when
        ExtractableResponse<Response> response = 지하철역_환승정보와_함께_목록_조회_요청();
        
        
        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_환승_정보_포함됨(response, Arrays.asList(일호선역과이호선역환승정보, 일호선역환승정보, 이호선역환승정보));
        지하철역_노선_등록_안된역은_환승_정보_포함_안됨(response, 노선에등록안됨역);
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

    @DisplayName("지하철역을 조회시 응답 목록은 가장 최근에 등록된 역 먼저 보여준다.")
    @Test
    void getStationsWithOrder() {
        // given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청();

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_순서가_보장됨(response, Arrays.asList(stationResponse2, stationResponse1));
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

    @DisplayName("사전에 등록되지 않은 역에 대해 삭제 요청은 처리할 수 없음.")
    @Test
    void deleteStationNotRegistered() {
        // when
        ExtractableResponse<Response> response = 지하철역_아이디로_제거_요청("100000");

        // then
        지하철역_등록되어있지_않음(response);
    }

    @DisplayName("구간에 등록된 역에 대해 삭제 요청은 처리할 수 없음.")
    @Test
    void deleteStationFailWhenRegisteredInSection() {
        // when
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역");
        StationResponse 광교역 = 지하철역_등록되어_있음("광교역");
        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10);

        ExtractableResponse<Response> response = 지하철역_제거_요청(강남역);

        // then
        지하철역_구간에_등록되어_있음(response);
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .auth().oauth2(loginSetUp().getAccessToken())
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

    public static ExtractableResponse<Response> 지하철역_환승정보와_함께_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/stations/transfer")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(loginSetUp().getAccessToken())
                .when().delete("/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_아이디로_제거_요청(String stationId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(loginSetUp().getAccessToken())
                .when().delete("/stations/" + stationId)
                .then().log().all()
                .extract();
    }

    public static void 지하철역_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철역_이름이_중복되어_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("이미 등록된 역입니다.");
    }

    public static void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("2자 이상 한글/숫자로 구성된 역 이름만 허용합니다.");
    }

    public static void 지하철역_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철역_등록되어있지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("역이 등록되어 있지 않습니다.");
    }

    public static void 지하철역_구간에_등록되어_있음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
        assertThat(errorResponse.getErrorMessage()).isEqualTo("노선에 등록된 역은 삭제할 수 없습니다.");
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

    public static void 지하철역_목록_순서가_보장됨(ExtractableResponse<Response> response, List<StationResponse> createdResponses) {
        List<String> realResponseName = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        List<String> expectedResponseName = createdResponses.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(realResponseName).isEqualTo(expectedResponseName);
    }

    public static void 지하철역_목록_환승_정보_포함됨(ExtractableResponse<Response> response, List<StationWithTransferResponse> createdResponses) {
        List<StationWithTransferResponse> transferInfoResponses =
                new ArrayList<>(response.jsonPath().getList(".", StationWithTransferResponse.class));

        final List<String> responseStationNames = transferInfoResponses.stream()
                .map(StationWithTransferResponse::getName)
                .collect(Collectors.toList());

        final List<String> expectedStationNames = createdResponses.stream()
                .map(StationWithTransferResponse::getName)
                .collect(Collectors.toList());

        assertThat(responseStationNames).containsAll(expectedStationNames);

        final List<List<String>> responseStationTransfer = transferInfoResponses.stream()
                .map(StationWithTransferResponse::getTransfer)
                .collect(Collectors.toList());

        final List<List<String>> expectedStationTransfer = createdResponses.stream()
                .map(StationWithTransferResponse::getTransfer)
                .collect(Collectors.toList());

        assertThat(responseStationTransfer).containsAll(expectedStationTransfer);
    }

    private static void 지하철역_노선_등록_안된역은_환승_정보_포함_안됨(ExtractableResponse<Response> response, StationResponse stationResponse) {
        List<StationWithTransferResponse> transferInfoResponses =
                new ArrayList<>(response.jsonPath().getList(".", StationWithTransferResponse.class));

        final List<String> responseStationNames = transferInfoResponses.stream()
                .map(StationWithTransferResponse::getName)
                .collect(Collectors.toList());

        assertThat(responseStationNames).doesNotContain(stationResponse.getName());
    }
}
