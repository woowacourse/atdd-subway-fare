package wooteco.subway.path;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL_OF_TEENAGER = EMAIL + "TEEN";
    private static final String EMAIL_OF_CHILD = EMAIL + "CHILD";
    private static final String EMAIL_OF_BABY = EMAIL + "BABY";
    private static final Integer TEENAGER = 13;
    private static final Integer CHILD = 6;
    private static final Integer BABY = 5;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 구호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private StationResponse 가양역;
    private StationResponse 등촌역;
    private StationResponse 염창역;
    private StationResponse 당산역;
    private StationResponse 여의도역;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    /**
     * <구호선>
     * 가양역 -9- 등촌역 -40- 염창역 -1- 당산역 -1- 여의도역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        가양역 = 지하철역_등록되어_있음("가양역");
        등촌역 = 지하철역_등록되어_있음("등촌역");
        염창역 = 지하철역_등록되어_있음("염창역");
        당산역 = 지하철역_등록되어_있음("당산역");
        여의도역 = 지하철역_등록되어_있음("여의도역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 20);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-700", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-800", 교대역, 양재역, 10);

        구호선 = 지하철_노선_등록되어_있음("구호선", "황금색", 가양역, 등촌역, 9);

        지하철_구간_등록되어_있음(구호선, 등촌역, 염창역, 40);
        지하철_구간_등록되어_있음(구호선, 염창역, 당산역, 1);
        지하철_구간_등록되어_있음(구호선, 당산역, 여의도역, 1);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다. - 10km")
    @Test
    void findPatAndFareWhenDistanceIs10() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(성인_사용자, 3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다 - 10km 초과 50km 미만")
    @Test
    void findPatAndFareWhenDistanceUnderBetween10to50() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(성인_사용자, 5L, 7L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역));
        총_거리가_응답됨(response, 49);
        총_요금이_응답됨(response, 2050);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다 - 50km")
    @Test
    void findPatAndFareWhenDistanceIs50() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(성인_사용자, 5L, 8L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역));
        총_거리가_응답됨(response, 50);
        총_요금이_응답됨(response, 2050);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회한다 - 50km 초과")
    @Test
    void findPatAndFareWhenDistanceIsOver50() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(성인_사용자, 5L, 9L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역, 여의도역));
        총_거리가_응답됨(response, 51);
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("비회원, 두 역의 최단 거리 경로와 요금을 조회한다. - 10km")
    @Test
    void findPatAndFareWhenDistanceIs10AndGuest() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(비회원, 3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("비회원, 두 역의 최단 거리 경로와 요금을 조회한다 - 10km 초과 50km 미만")
    @Test
    void findPatAndFareWhenDistanceUnderBetween10to50AndGuest() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(비회원, 5L, 7L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역));
        총_거리가_응답됨(response, 49);
        총_요금이_응답됨(response, 2050);
    }

    @DisplayName("비회원, 두 역의 최단 거리 경로와 요금을 조회한다 - 50km")
    @Test
    void findPatAndFareWhenDistanceIs50AndGuest() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(비회원, 5L, 8L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역));
        총_거리가_응답됨(response, 50);
        총_요금이_응답됨(response, 2050);
    }

    @DisplayName("비회원, 두 역의 최단 거리 경로와 요금을 조회한다 - 50km 초과")
    @Test
    void findPatAndFareWhenDistanceIsOver50AndGuest() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(비회원, 5L, 9L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역, 여의도역));
        총_거리가_응답됨(response, 51);
        총_요금이_응답됨(response, 2150);
    }

    @DisplayName("청소년, 두 역의 최단 거리 경로와 요금을 조회한다. - 10km")
    @Test
    void findPatAndFareWhenDistanceIs10AndTeenager() {
        //when
        TokenResponse 청소년 = 로그인(EMAIL_OF_TEENAGER, PASSWORD, TEENAGER);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(청소년, 3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 720);
    }

    @DisplayName("청소년, 두 역의 최단 거리 경로와 요금을 조회한다 - 10km 초과 50km 미만")
    @Test
    void findPatAndFareWhenDistanceUnderBetween10to50AndTeenager() {
        //when
        TokenResponse 청소년 = 로그인(EMAIL_OF_TEENAGER, PASSWORD, TEENAGER);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(청소년, 5L, 7L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역));
        총_거리가_응답됨(response, 49);
        총_요금이_응답됨(response, 1360);
    }

    @DisplayName("청소년, 두 역의 최단 거리 경로와 요금을 조회한다 - 50km")
    @Test
    void findPatAndFareWhenDistanceIs50AndTeenager() {
        //when
        TokenResponse 청소년 = 로그인(EMAIL_OF_TEENAGER, PASSWORD, TEENAGER);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(청소년, 5L, 8L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역));
        총_거리가_응답됨(response, 50);
        총_요금이_응답됨(response, 1360);
    }

    @DisplayName("청소년, 두 역의 최단 거리 경로와 요금을 조회한다 - 50km 초과")
    @Test
    void findPatAndFareWhenDistanceIsOver50AndTeenager() {
        //when
        TokenResponse 청소년 = 로그인(EMAIL_OF_TEENAGER, PASSWORD, TEENAGER);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(청소년, 5L, 9L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역, 여의도역));
        총_거리가_응답됨(response, 51);
        총_요금이_응답됨(response, 1440);
    }

    @DisplayName("어린이, 두 역의 최단 거리 경로와 요금을 조회한다. - 10km")
    @Test
    void findPatAndFareWhenDistanceIs10AndChild() {
        //when
        TokenResponse 어린이 = 로그인(EMAIL_OF_CHILD, PASSWORD, CHILD);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(어린이, 3L, 2L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response, 10);
        총_요금이_응답됨(response, 450);
    }

    @DisplayName("어린이, 두 역의 최단 거리 경로와 요금을 조회한다 - 10km 초과 50km 미만")
    @Test
    void findPatAndFareWhenDistanceUnderBetween10to50AndChild() {
        //when
        TokenResponse 어린이 = 로그인(EMAIL_OF_CHILD, PASSWORD, CHILD);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(어린이, 5L, 7L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역));
        총_거리가_응답됨(response, 49);
        총_요금이_응답됨(response, 850);
    }

    @DisplayName("어린이, 두 역의 최단 거리 경로와 요금을 조회한다 - 50km")
    @Test
    void findPatAndFareWhenDistanceIs50AndChild() {
        //when
        TokenResponse 어린이 = 로그인(EMAIL_OF_CHILD, PASSWORD, CHILD);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(어린이, 5L, 8L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역));
        총_거리가_응답됨(response, 50);
        총_요금이_응답됨(response, 850);
    }

    @DisplayName("어린이, 두 역의 최단 거리 경로와 요금을 조회한다 - 50km 초과")
    @Test
    void findPatAndFareWhenDistanceIsOver50AndChild() {
        //when
        TokenResponse 어린이 = 로그인(EMAIL_OF_CHILD, PASSWORD, CHILD);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(어린이, 5L, 9L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역, 여의도역));
        총_거리가_응답됨(response, 51);
        총_요금이_응답됨(response, 900);
    }

    @DisplayName("유아, 두 역의 최단 거리 경로와 요금을 조회한다 - 50km 초과")
    @Test
    void findPatAndFareWhenDistanceIsOver50AndBaby() {
        //when
        TokenResponse 어린이 = 로그인(EMAIL_OF_BABY, PASSWORD, BABY);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(어린이, 5L, 9L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(가양역, 등촌역, 염창역, 당산역, 여의도역));
        총_거리가_응답됨(response, 51);
        총_요금이_응답됨(response, 0);
    }

    @DisplayName("구간 추가 요금이 존재하는 경우 요금에 더해진다.")
    @Test
    void testFindPathAndFareWhenExistsExtraFare() {
        // when
        LineRequest lineRequest = new LineRequest("추가선", "흰색", 9L, 1L, 49, 900);
        지하철_노선_등록되어_있음(lineRequest);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(성인_사용자, 9L, 1L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(여의도역, 강남역));
        총_거리가_응답됨(response, 49);
        총_요금이_응답됨(response, 2950);
    }

    @DisplayName("구간 추가 요금이 존재하는 경우 요금에 더해진다. - 청소년")
    @Test
    void testFindPathAndFareWhenExistsExtraFareAndTeenager() {
        // when
        LineRequest lineRequest = new LineRequest("추가선", "흰색", 9L, 1L, 49, 900);
        지하철_노선_등록되어_있음(lineRequest);
        TokenResponse 청소년 = 로그인(EMAIL_OF_TEENAGER, PASSWORD, TEENAGER);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(청소년, 9L, 1L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(여의도역, 강남역));
        총_거리가_응답됨(response, 49);
        총_요금이_응답됨(response, 2080);
    }

    @DisplayName("구간 추가 요금이 존재하는 경우 요금에 더해진다. - 청소년")
    @Test
    void testFindPathAndFareWhenExistsExtraFareAndChild() {
        // when
        LineRequest lineRequest = new LineRequest("추가선", "흰색", 9L, 1L, 49, 900);
        지하철_노선_등록되어_있음(lineRequest);
        TokenResponse 어린이 = 로그인(EMAIL_OF_CHILD, PASSWORD, CHILD);
        ExtractableResponse<Response> response = 거리_경로_조회_요청(어린이, 9L, 1L);

        //then
        적절한_경로_응답됨(response, Lists.newArrayList(여의도역, 강남역));
        총_거리가_응답됨(response, 49);
        총_요금이_응답됨(response, 1300);
    }

    private void 총_요금이_응답됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDefaultFare()).isEqualTo(fare);
    }

    public static ExtractableResponse<Response> 거리_경로_조회_요청(TokenResponse token,  long source, long target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    public static void 적절한_경로_응답됨(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    private TokenResponse 로그인(String email, String password, int age) {
        회원_생성을_요청(email, password, age);
        TokenResponse response = 로그인되어_있음(email, password);
        return response;
    }
}
