package wooteco.subway.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

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
        ExtractableResponse<Response> response = 지하철역_생성_요청(name);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("지하철역 생성 - 역 이름: 2자 이상 20자 이하의 한글, 숫자로만 이루어져 있다.(공백 허용 X) - 예외상황 400 에러")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "일", "asdf", "일 ", "하나 역", "하나a역", "하나,역", "하나\t역", "일이삼사오육칠팔구십일이삼사오육칠팔구십일"})
    void createStationWithNotValidName(String name) {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(name);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

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
        지하철역_목록_응답됨(response);
        지하철역_목록_포함됨(response, Arrays.asList(stationResponse1, stationResponse2));
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

    @DisplayName("존재하지 않는 지하철역을 제거한다. - 404 에러")
    @Test
    void deleteStationNotExists() {
        // given
        Long stationIdNotExists = -1L;

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청_Id로(stationIdNotExists);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
            .given(spec).log().all()
            .filter(document("create-station"))
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return RestAssured
            .given(spec).log().all()
            .filter(document("get-all-stations"))
            .when().get("/stations")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse) {
        return RestAssured
            .given(spec).log().all()
            .filter(document("delete-station"))
            .when().delete("/stations/" + stationResponse.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청_Id로(Long id) {
        return RestAssured
            .given(spec).log().all()
            .filter(document("delete-station"))
            .when().delete("/stations/" + id)
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
