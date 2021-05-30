package wooteco.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.DocsIdentifier;
import wooteco.subway.RestDocs;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.DocsIdentifier.*;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceRestDocs extends RestDocs {
    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역, STATIONS_POST_SUCCESS);

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("지하철 역은 2자 이상 20자 이하의 한글 (숫자 포함. 공백 허용 X)")
    @Test
    void createErrorNameStation() throws Exception {
        //given
        String numberName = "공백포함 역";

        //when
        ExtractableResponse<Response> response = 지하철역_생성_요청(numberName, STATIONS_POST_FAIL_NAME_BLANK);

        //then
        지하철역_생성되지_않음(response);

        //given
        numberName = "역";

        //when
        response = 지하철역_생성_요청(numberName, DocsIdentifier.STATIONS_POST_FAIL_NAME_LENGTH);

        //then
        지하철역_생성되지_않음(response);

        //given
        numberName = "스무자넘는매우매우매우매우매우매우진짜이상하고정말존재하지않을것같은역";

        //when
        response = 지하철역_생성_요청(numberName, STATIONS_POST_FAIL_NAME_LENGTH);

        //then
        지하철역_생성되지_않음(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역, STATIONS_POST_FAIL_DUPLICATED);

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

    @DisplayName("지하철 역 이름을 수정한다.")
    @Test
    void updateStationName_success() throws Exception {
        //given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        //when
        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse.getId(), "송파나루역", STATIONS_PUT_SUCCESS);

        //then
        지하철역_수정됨(response);
    }

    @DisplayName("지하철 역 이름 수정 실패한다. - 이름 중복")
    @Test
    void updateStationName_fail_duplicated() throws Exception {
        //given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        //when
        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse.getId(), "강남역", STATIONS_PUT_FAIL_DUPLICATED);

        //then
        지하철역_수정_실패(response);
    }

    @DisplayName("역 이름 수정시 이름 유효성 검사 한다. - 지하철 역은 2자 이상 20자 이하의 한글 (숫자 포함. 공백 허용 X)")
    @Test
    void updateErrorNameStation() throws Exception {
        //given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        String errorName = "공백포함 역";

        //when
        ExtractableResponse<Response> response = 지하철역_수정_요청(stationResponse.getId(), errorName,STATIONS_PUT_FAIL_NAME_BLANK);

        //then
        지하철역_수정_실패(response);

        //given
        errorName = "역";

        //when
        response = 지하철역_수정_요청(stationResponse.getId(), errorName, STATIONS_PUT_FAIL_NAME_LENGTH);

        //then
        지하철역_수정_실패(response);

        //given
        errorName = "스무자넘는매우매우매우매우매우매우진짜이상하고정말존재하지않을것같은역";

        //when
        response = 지하철역_수정_요청(stationResponse.getId(), errorName, STATIONS_PUT_FAIL_NAME_LENGTH);

        //then
        지하철역_수정_실패(response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationResponse stationResponse = 지하철역_등록되어_있음(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse, STATIONS_DELETE_SUCCESS);

        // then
        지하철역_삭제됨(response);
    }

    @DisplayName("지하철 역 삭제 실패한다. - 이미 구간에 등록된 지하쳘 역")
    @Test
    void deleteStation_fail_already_assigned() throws Exception {
        //given
        StationResponse stationResponse1 = 지하철역_등록되어_있음(강남역);
        StationResponse stationResponse2 = 지하철역_등록되어_있음(역삼역);
        지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", stationResponse1.getId(), stationResponse2.getId(), 10));

        //when
        ExtractableResponse<Response> response = 지하철역_제거_요청(stationResponse1, STATIONS_DELETE_FAIL);
        //then
        지하철역_삭제_실패됨(response);
    }

    public static ExtractableResponse<Response> 지하철역_생성_요청(String name, String identifier) {
        StationRequest stationRequest = new StationRequest(name);

        return  given(identifier)
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_목록_조회_요청() {
        return  given(STATIONS_GET)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(StationResponse stationResponse, String identifier) {
        return  given(identifier)
                .when().delete("/stations/" + stationResponse.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철역_수정_요청(Long id, String name, String identifier) {
        StationRequest stationRequest = new StationRequest(name);
        return  given(identifier)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().put("/stations/" + id)
                .then().log().all()
                .extract();
    }
}
