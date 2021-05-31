package wooteco.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import wooteco.subway.RestDocs;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;

import static wooteco.subway.DocsIdentifier.*;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.*;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceRestDocs extends RestDocs {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance, String identifier) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return given(identifier)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station, String identifier) {
        return given(identifier)
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        정자역 = 지하철역_등록되어_있음("정자역");
        광교역 = 지하철역_등록되어_있음("광교역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10, 900L);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 양재역, 3, SECTIONS_POST_SUCCESS);

        // then
        지하철_구간_생성됨(response, 신분당선, Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 광교역, 3, SECTIONS_POST_FAIL_EVERY);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 양재역, 3, SECTIONS_POST_FAIL_ANY);

        // then
        지하철_구간_등록_실패됨(response);
    }

    @DisplayName("지하철 노선에 등록된 지하철역을 제외한다.")
    @Test
    void removeLineSection1() {
        // given
        SectionAcceptanceTest.지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2);
        SectionAcceptanceTest.지하철_구간_생성_요청(신분당선, 양재역, 정자역, 2);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역, SECTIONS_DELETE_SUCCESS);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse, 신분당선, Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역, SECTIONS_DELETE_FAIL);

        // then
        지하철_노선에_지하철역_제외_실패됨(removeResponse);
    }
}
