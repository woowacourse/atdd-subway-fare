package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_생성_요청;
import static wooteco.subway.member.MemberAcceptanceTest.응답코드_확인;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역 생성 - 역 이름: 2자 이상 20자 이하의 한글, 숫자로만 이루어져 있다.(공백 허용 X) - 생성 성공")
    @ParameterizedTest
    @ValueSource(strings = {"하나", "11", "하나2역", "일이삼사오육칠팔구십일이삼사오육칠팔구십"})
    void createStation(String name) {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(name, "create-station");

        // then
        응답코드_확인(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("지하철역 생성 - 역 이름: 2자 이상 20자 이하의 한글, 숫자로만 이루어져 있다.(공백 허용 X) - 예외상황 400 에러")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "일", "asdf", "일 ", "하나 역", "하나a역", "하나,역", "하나\t역", "일이삼사오육칠팔구십일이삼사오육칠팔구십일"})
    void createStationWithNotValidName(String name) {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(name, "create-station-fail-invalid-name");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        ExtractableResponse<Response> oldCreateResponse = 지하철역_생성_요청(강남역, "create-station");
        응답코드_확인(oldCreateResponse, HttpStatus.CREATED);

        // when
        ExtractableResponse<Response> newCreateResponse = 지하철역_생성_요청(강남역, "create-station-fail-name-duplicate");

        // then
        응답코드_확인(newCreateResponse, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지하철역 전체 목록을 조회한다.")
    @Test
    void getAllStations() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철역_생성_요청(강남역, "create-station");
        응답코드_확인(createResponse1, HttpStatus.CREATED);
        StationResponse stationResponse1 = 지하철역_생성_응답으로부터_StationResponse_추출(createResponse1);

        ExtractableResponse<Response> createResponse2 = 지하철역_생성_요청(역삼역, "create-station");
        응답코드_확인(createResponse2, HttpStatus.CREATED);
        StationResponse stationResponse2 = 지하철역_생성_응답으로부터_StationResponse_추출(createResponse2);

        // when
        ExtractableResponse<Response> getAllLinesListResponse = 지하철역_목록_조회_요청("get-all-stations");
        응답코드_확인(getAllLinesListResponse, HttpStatus.OK);

        // then
        응답코드_확인(getAllLinesListResponse, HttpStatus.OK);
        지하철역_목록_포함됨(getAllLinesListResponse, Arrays.asList(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역, "create-station");
        응답코드_확인(createResponse, HttpStatus.CREATED);
        StationResponse stationResponse = 지하철역_생성_응답으로부터_StationResponse_추출(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철역_제거_요청_Id로(stationResponse.getId(), "delete-station");

        // then
        응답코드_확인(deleteResponse, HttpStatus.NO_CONTENT);
    }

    @DisplayName("존재하지 않는 지하철역을 제거한다. - 404 에러")
    @Test
    void deleteStationNotExists() {
        // given
        Long stationIdNotExists = 100L;

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청_Id로(stationIdNotExists, "delete-station-fail-not-exists");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("노선에 포함된 역을 삭제 - 404 에러")
    @Test
    void deleteStationInLine() {
        // given
        ExtractableResponse<Response> stationCreateResponse1 = 지하철역_생성_요청("강남역", "create-station");
        응답코드_확인(stationCreateResponse1, HttpStatus.CREATED);
        StationResponse stationResponse1 = 지하철역_생성_응답으로부터_StationResponse_추출(stationCreateResponse1);

        ExtractableResponse<Response> stationCreateResponse2 = 지하철역_생성_요청("광교역", "create-station");
        응답코드_확인(stationCreateResponse2, HttpStatus.CREATED);
        StationResponse stationResponse2 = 지하철역_생성_응답으로부터_StationResponse_추출(stationCreateResponse2);

        ExtractableResponse<Response> lineCreateResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600", stationResponse1, stationResponse2, 10, 0, "create-line");
        응답코드_확인(lineCreateResponse, HttpStatus.CREATED);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철역_제거_요청_Id로(1L, "delete-station-fail-in-line");

        // then
        응답코드_확인(deleteResponse, HttpStatus.BAD_REQUEST);
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static StationResponse 지하철역_생성_응답으로부터_StationResponse_추출(ExtractableResponse<Response> response) {
        return response.as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);
        return RestAssured
            .given(spec).log().all()
            .filter(document("create-station", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, String docsIdentifier) {
        StationRequest stationRequest = new StationRequest(name);
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static void 지하철역_생성_요청_실패_형식에_맞지_않는_역_이름(String name) {
        StationRequest stationRequest = new StationRequest(name);

        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .filter(document("create-station-fail-invalid-name", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_생성_요청_실패_이미_존재하는_역_이름(String name) {
        StationRequest stationRequest = new StationRequest(name);

        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .filter(document("create-station-fail-name-duplicate", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청(String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().delete("/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(Long stationId, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().delete("/stations/" + stationId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청_Id로(Long id, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().delete("/stations/" + id)
            .then().log().all()
            .extract();
    }

    public static void 지하철역_Id로_제거_요청_Id로_실패_존재하지_않는_지하철역(Long id) {
        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .filter(document("delete-station-fail-not-exists", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().delete("/stations/" + id)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철역_제거_요청_Id로_실패_노선에_포함된_역(Long id) {
        ExtractableResponse<Response> response = RestAssured
            .given(spec).log().all()
            .filter(document("delete-station-fail-in-line", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().delete("/stations/" + id)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    public static void 지하철역_목록_포함됨(ExtractableResponse<Response> getAllLinesListResponse, List<StationResponse> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> actualLineIds = getAllLinesListResponse.jsonPath().getList(".", StationResponse.class).stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(actualLineIds).containsExactlyInAnyOrderElementsOf(expectedLineIds);
    }
}
