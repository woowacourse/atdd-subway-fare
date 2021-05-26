package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_조회_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 일호선;
    private StationResponse A역;
    private StationResponse B역;
    private StationResponse C역;
    private StationResponse D역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        A역 = 지하철역_등록되어_있음("A역");
        C역 = 지하철역_등록되어_있음("B역");
        D역 = 지하철역_등록되어_있음("C역");
        B역 = 지하철역_등록되어_있음("D역");

        일호선 = 지하철_노선_등록되어_있음("일호선", A역, B역, 3);
    }

    @DisplayName("구간등록: 노선 중간에 역을 추가")
    @Test
    void addLineSection() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(일호선, A역, C역, 1);

        // then
        지하철_구간_생성됨(response, 일호선, Arrays.asList(A역, C역, B역));
    }

    @DisplayName("구간등록: 노선 종점에 역을 추가")
    @Test
    void addLineSection2() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(일호선, C역, A역, 1);

        // then
        지하철_구간_생성됨(response, 일호선, Arrays.asList(C역, A역, B역));
    }

    @DisplayName("구간등록-실패: 상/하행역 둘다 노선에 이미 등록됨")
    @Test
    void addLineSectionWithSameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(일호선, A역, B역, 1);

        // then
        지하철_구간_등록_응답코드_비교(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("구간등록-실패: 상/하행역 둘다 노선에 등록되지 않음")
    @Test
    void addLineSectionWithNoStation() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(일호선, C역, D역, 1);

        // then
        지하철_구간_등록_응답코드_비교(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("구간등록-실패: 등록하려는 구간이 기존구간 거리보다 같거나 큼")
    @Test
    void addLineSectionWithLongerDistance() {
        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(일호선, A역, C역, 3);

        // then
        지하철_구간_등록_응답코드_비교(response, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("구간제거: 노선에 등록된 지하철역을 제외한다")
    @Test
    void removeLineSection1() {
        // given
        지하철_구간_생성_요청(일호선, A역, C역, 1);

        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제거_요청(일호선, C역);

        // then
        지하철_노선에_지하철역_제외됨(removeResponse, 일호선, Arrays.asList(A역, B역));
    }

    @DisplayName("구간제거-실패: 노선에 등록된 역이 두개일 때 한 역을 제거")
    @Test
    void removeLineSection2() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제거_요청(일호선, A역);

        // then
        지하철_구간_등록_응답코드_비교(removeResponse, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("구간제거-실패: 노선에 등록되지 않은 역을 제거")
    @Test
    void removeLineSection3() {
        // when
        ExtractableResponse<Response> removeResponse = 지하철_노선에_지하철역_제거_요청(일호선, C역);

        // then
        지하철_구간_등록_응답코드_비교(removeResponse, HttpStatus.BAD_REQUEST);
    }


    public static ExtractableResponse<Response> 지하철_구간_생성_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제거_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    private void 지하철_구간_생성됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> result, LineResponse lineResponse, List<StationResponse> stationResponses) {
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponse);
        지하철_노선에_지하철역_순서_정렬됨(response, stationResponses);
    }

    public static void 지하철_구간_등록_응답코드_비교(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
