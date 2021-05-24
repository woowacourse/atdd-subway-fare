package wooteco.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.station.dto.StationResponse;

import static wooteco.subway.auth.AuthAcceptanceTest.로그인되어_있음;
import static wooteco.subway.auth.AuthAcceptanceTest.회원_등록되어_있음;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.path.PathTest.*;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회 - 사용자 나이에 따른 요금할인")
public class PathFareByMemberAgeTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private StationResponse A역;
    private StationResponse B역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        A역 = 지하철역_등록되어_있음("A역");
        B역 = 지하철역_등록되어_있음("B역");

        지하철_노선_등록되어_있음("1호선", A역, B역, 10);
    }

    @DisplayName("나이에 따른 요금 할인: 성인")
    @Test
    void fareDiscount_adult() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, 19);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 토큰인증하여_거리_경로_조회_요청(A역.getId(), B역.getId(), tokenResponse);

        // then
        총_요금이_응답됨(response, 1250);
    }

    @DisplayName("나이에 따른 요금 할인: 청소년")
    @Test
    void fareDiscount_teenager1() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, 13);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 토큰인증하여_거리_경로_조회_요청(A역.getId(), B역.getId(), tokenResponse);

        // then
        총_요금이_응답됨(response, 720);
    }

    @DisplayName("나이에 따른 요금 할인: 청소년")
    @Test
    void fareDiscount_teenager2() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, 18);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 토큰인증하여_거리_경로_조회_요청(A역.getId(), B역.getId(), tokenResponse);

        // then
        총_요금이_응답됨(response, 720);
    }

    @DisplayName("나이에 따른 요금 할인: 어린이")
    @Test
    void fareDiscount_children1() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, 6);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 토큰인증하여_거리_경로_조회_요청(A역.getId(), B역.getId(), tokenResponse);

        // then
        총_요금이_응답됨(response, 450);
    }

    @DisplayName("나이에 따른 요금 할인: 어린이")
    @Test
    void fareDiscount_children2() {
        // given
        회원_등록되어_있음(EMAIL, PASSWORD, 12);
        TokenResponse tokenResponse = 로그인되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 토큰인증하여_거리_경로_조회_요청(A역.getId(), B역.getId(), tokenResponse);

        // then
        총_요금이_응답됨(response, 450);
    }

    @DisplayName("나이에 따른 요금 할인: 비로그인(성인요금)")
    @Test
    void fareDiscount_nonLoginMember() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(A역.getId(), B역.getId());

        // then
        총_요금이_응답됨(response, 1250);
    }
}
