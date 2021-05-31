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
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String loginToken;

    private ExtractableResponse<Response> findPath(long source, long target, String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all()
                .extract();
    }

    private void assertPathContains(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    private void assertPathDistance(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    private void assertPathFare(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        registerMember("kevin@naver.com", "123", 27);
        loginToken = login("kevin@naver.com", "123").getAccessToken();

        강남역 = createStation("강남역", loginToken).as(StationResponse.class);
        양재역 = createStation("양재역", loginToken).as(StationResponse.class);
        교대역 = createStation("교대역", loginToken).as(StationResponse.class);
        남부터미널역 = createStation("남부터미널역", loginToken).as(StationResponse.class);

        신분당선 = createLine(new LineRequest("신분당선", "bg-redd-600", 강남역.getId(), 양재역.getId(), 10), loginToken).as(LineResponse.class);
        이호선 = createLine(new LineRequest("이호선", "bg-redd-601", 교대역.getId(), 강남역.getId(), 10), loginToken).as(LineResponse.class);
        삼호선 = createLine(new LineRequest("삼호선", "bg-redd-603", 교대역.getId(), 양재역.getId(), 5), loginToken).as(LineResponse.class);

        addSection(삼호선.getId(), new SectionRequest(교대역.getId(), 남부터미널역.getId(), 3), loginToken);
    }

    @DisplayName("비회원으로 두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", 3L, 2L)
                .then().log().all()
                .extract();

        assertPathContains(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertPathDistance(response, 5);
        assertPathFare(response, 1250);
    }

    @DisplayName("아동 나이 회원으로 경로 조회를 요청한다.")
    @Test
    void findPathWhenChildren() {
        registerMember("abc@naver.com", "pass", 8);
        TokenResponse token = login("abc@naver.com", "pass");

        ExtractableResponse<Response> response = findPath(3L, 2L, token.getAccessToken());

        assertPathContains(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertPathDistance(response, 5);
        assertPathFare(response, 450);
    }

    @DisplayName("청소년 나이 회원으로 경로 조회를 요청한다.")
    @Test
    void findPathWhenTeenager() {
        registerMember("abc@naver.com", "pass", 16);
        TokenResponse token = login("abc@naver.com", "pass");

        ExtractableResponse<Response> response = findPath(3L, 2L, token.getAccessToken());

        assertPathContains(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertPathDistance(response, 5);
        assertPathFare(response, 720);
    }

    @DisplayName("성인 나이 회원으로 경로 조회를 요청한다.")
    @Test
    void findPathWhenAdult() {
        ExtractableResponse<Response> response = findPath(3L, 2L, loginToken);

        assertPathContains(response, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertPathDistance(response, 5);
        assertPathFare(response, 1250);
    }
}
