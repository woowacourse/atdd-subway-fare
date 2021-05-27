package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_조회_요청;
import static wooteco.subway.member.MemberAcceptanceTest.응답코드_확인;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private LineResponse 신분당선;

    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        정자역 = 지하철역_등록되어_있음("정자역");
        광교역 = 지하철역_등록되어_있음("광교역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 광교역, 10);
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addSection() {
        // when
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 3, "create-section");

        // then
        지하철_노선_구간_순서까지_정확히_확인(신분당선.getId(), Arrays.asList(강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 구간을 두 번 등록한다.")
    @Test
    void addSectionsTwice() {
        // when
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2, "create-section");
        지하철_구간_생성_요청(신분당선, 정자역, 강남역, 5, "create-section");

        // then
        지하철_노선_구간_순서까지_정확히_확인(신분당선.getId(), Arrays.asList(정자역, 강남역, 양재역, 광교역));
    }

    @DisplayName("지하철 노선에 이미 존재하는 구간을 등록한다. - 400 에러 응답")
    @Test
    void addLineSectionWithSameSectionError() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 강남역, 광교역, 3, "create-section-fail-same-section");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
        지하철_노선_구간_순서까지_정확히_확인(신분당선.getId(), Arrays.asList(강남역, 광교역));
    }

    @DisplayName("지하철 노선에 모두 존재하지 않는 역들로 구성된 구간을 등록한다. - 400 에러 응답")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(신분당선, 정자역, 양재역, 3, "create-section-fail-all-no-section");

        // then
        응답코드_확인(response, HttpStatus.BAD_REQUEST);
        지하철_노선_구간_순서까지_정확히_확인(신분당선.getId(), Arrays.asList(강남역, 광교역));
    }

    @DisplayName("지하철 노선에 등록된 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        지하철_구간_생성_요청(신분당선, 강남역, 양재역, 2, "create-section");
        지하철_구간_생성_요청(신분당선, 양재역, 정자역, 2, "create-section");

        // when
        지하철_노선에_지하철역_제외_요청(신분당선, 양재역, "delete-section");

        // then
        지하철_노선_구간_순서까지_정확히_확인(신분당선.getId(), Arrays.asList(강남역, 정자역, 광교역));
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void delete_LineSectionFail_When_AllSectionStations_Contained() {
        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선에_지하철역_제외_요청(신분당선, 강남역, "delete-section-fail-line-has-only-one-section");

        // then
        응답코드_확인(deleteResponse, HttpStatus.BAD_REQUEST);
        지하철_노선_구간_순서까지_정확히_확인(신분당선.getId(), Arrays.asList(강남역, 광교역));
    }

    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance, String docsIdentifier) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        return RestAssured
            .given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .body(sectionRequest)
            .when().post("/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station, String docsIdentifier) {
        return RestAssured
            .given(spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .filter(document(docsIdentifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
            .then().log().all()
            .extract();
    }

    public static void 지하철_노선_구간_순서까지_정확히_확인(Long lineId, List<StationResponse> stationResponses) {
        ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(lineId, "get-line");
        지하철_노선에_지하철역_순서_정렬됨(getLineResponse, stationResponses);
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> getLineResponse, List<StationResponse> expectedStationsInOrder) {
        LineResponse line = getLineResponse.as(LineResponse.class);

        List<Long> actualStationIdsInOrder = line.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedStationIdsInOrder = expectedStationsInOrder.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(actualStationIdsInOrder).containsExactlyElementsOf(expectedStationIdsInOrder);
    }
}
