package wooteco.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.Documentation;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StationDocument extends Documentation {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청_성공(강남역);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성하는 경우 예외처리된다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청_실패(강남역);

        // then
        지하철역_생성_실패됨(response);
        assertThat((String) response.body().jsonPath().get("message")).isEqualTo("이미 존재하는 지하철 역입니다.");
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

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청_성공(stationResponse);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("존재하지 않는 지하철역을 제거하려고 하면 예외가 발생한다.")
    @Test
    void deleteStationWithWrongId() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청_실패(new StationResponse(2L, 역삼역));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat((String) response.body().jsonPath().get("message")).isEqualTo("존재하지 않는 지하철 역입니다.");
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void updateStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청_성공(stationResponse.getId(), new StationRequest(역삼역));

        // then
        지하철역_수정됨(response);
    }

    @DisplayName("존재하지 않는 지하철역을 수정 시 예외가 발생한다.")
    @Test
    void updateStationWithWrongId() {
        // given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청_실패(2L, new StationRequest(역삼역));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat((String) response.body().jsonPath().get("message")).isEqualTo("존재하지 않는 지하철 역입니다.");
    }

    @DisplayName("이미 존재하는 지하철역 이름으로 수정 시 예외가 발생한다.")
    @Test
    void updateStationWithDuplicateName() {
        // given
        지하철역_등록되어_있음(강남역);
        StationResponse stationResponse = 지하철역_등록되어_있음(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청_실패(stationResponse.getId(), new StationRequest(강남역));

        // then
        지하철역_수정되지_않음(response);
        assertThat((String) response.body().jsonPath().get("message")).isEqualTo("이미 존재하는 지하철 역입니다.");
    }

    private void 지하철역_삭제되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철역_수정되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철역_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철역_수정_요청_성공(Long id, StationRequest stationRequest) {
        return given("station/edit/success")
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/stations/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_수정_요청_실패(Long id, StationRequest stationRequest) {
        return given("station/edit/fail")
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/stations/" + id)
                .then().log().all()
                .extract();
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청_성공(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청_성공(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return given("station/create/success")
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청_실패(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return given("station/create/fail")
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return given("station/list")
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청_성공(StationResponse stationResponse) {
        return given("station/delete/success")
                .when().delete("/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청_실패(StationResponse stationResponse) {
        return given("station/delete/fail")
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
