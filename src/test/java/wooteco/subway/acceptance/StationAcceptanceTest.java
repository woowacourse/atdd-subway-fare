package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.AcceptanceTest;
import wooteco.subway.web.dto.request.StationRequest;
import wooteco.subway.web.dto.response.StationResponse;
import wooteco.utils.RestDocsUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.auth.acceptance.AuthAcceptanceTest.*;
import static wooteco.utils.RestDocsUtils.getRequestPreprocessor;
import static wooteco.utils.RestDocsUtils.getResponsePreprocessor;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @BeforeEach
    public void setUpData() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역, "station-create");

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("지하철역을 생성 실패 - 기존에 존재하는 이름으로 생성 요청")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역, "station-create-duplicate");

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
        ExtractableResponse<Response> response = 지하철역_목록_조회_요청("station-find");

        // then
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void updateStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse, 역삼역, "station-update");

        // then
        지하철역_수정됨(response, 역삼역);
    }

    @DisplayName("지하철역을 수정 실패 - 기존에 존재하는 이름으로 수정 요청")
    @Test
    void updateStationWithDuplicateName() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse, 강남역, "station-update-duplicate");

        // then
        지하철역_수정_실패(response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse, "station-delete");

        // then
        지하철역_삭제됨(response);
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰().getAccessToken())
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, String documentIdentifier) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given(RestDocsUtils.getRequestSpecification()).log().all()
                .auth().oauth2(토큰().getAccessToken())
                .body(stationRequest)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청(String documentIdentifier) {
        return RestAssured
                .given(RestDocsUtils.getRequestSpecification()).log().all()
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().get("/api/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_수정_요청(StationResponse stationResponse, String name, String documentIdentifier) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given(RestDocsUtils.getRequestSpecification()).log().all()
                .auth().oauth2(토큰().getAccessToken())
                .body(stationRequest)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse, String documentIdentifier) {
        return RestAssured
                .given(RestDocsUtils.getRequestSpecification()).log().all()
                .auth().oauth2(토큰().getAccessToken())
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
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

    public static void 지하철역_수정됨(ExtractableResponse<Response> response, String updateName) {
        String actualName = response.jsonPath().getJsonObject("name");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualName).isEqualTo(updateName);
    }

    public static void 지하철역_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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
}
