package wooteco.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;
import wooteco.utils.DocumentUtils;

import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.utils.DocumentUtils.*;

public final class StationRestAssuredRequestUtils {
    private StationRestAssuredRequestUtils() {
    }

    public static StationResponse 지하철역_등록되어_있음(String name) {
        return 지하철역_생성_요청(name).as(StationResponse.class);
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

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, String documentIdentifier) {
        StationRequest stationRequest = new StationRequest(name);

        return RestAssured
                .given(DocumentUtils.getRequestSpecification()).log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청(String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification()).log().all()
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse, String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification()).log().all()
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().delete("/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }
}
