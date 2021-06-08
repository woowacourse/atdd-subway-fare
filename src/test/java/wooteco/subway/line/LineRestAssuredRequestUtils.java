package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.station.dto.StationResponse;
import wooteco.utils.DocumentUtils;

import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.utils.DocumentUtils.*;

public class LineRestAssuredRequestUtils {
    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params, String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification())
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().post("/lines")
                .then().log().all().
                        extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청(String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification())
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponse response, String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification())
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().get("/lines/{lineId}", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponse response, LineRequest params, String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification())
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().put("/lines/" + response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponse lineResponse, String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification())
                .log().all()
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().delete("/lines/" + lineResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station, String documentIdentifier) {
        return RestAssured
                .given(DocumentUtils.getRequestSpecification())
                .log().all()
                .filter(document(documentIdentifier, getRequestPreprocessor(), getResponsePreprocessor()))
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequest);
    }

    public static LineResponse 지하철_노선_등록되어_있음(LineRequest lineRequest) {
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }
}
