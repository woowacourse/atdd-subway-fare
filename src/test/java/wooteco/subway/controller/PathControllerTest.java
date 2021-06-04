package wooteco.subway.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.ui.PathController;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("경로 관련 테스트")
@WebMvcTest(controllers = PathController.class)
public class PathControllerTest extends ControllerTest {
    public static final long SOURCE_STATION_ID = 1L;
    public static final long TARGET_STATION_ID = 2L;
    public static final int DISTANCE = 30;
    public static final int FARE = 2800;
    public static final int AGE = 12;
    public static final String EMAIL = "email@eamil.com";
    public static final Station 강남역 = new Station(SOURCE_STATION_ID, "강남역");
    public static final Station 잠실역 = new Station(TARGET_STATION_ID, "잠실역");
    public static final Station 삼전역 = new Station(3L, "삼전역");

    @MockBean
    private PathService pathService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("조회 - 성공")
    public void showPath() throws Exception {
        //given
        PathResponse pathResponse = new PathResponse(StationResponse.listOf(Arrays.asList(강남역, 잠실역, 삼전역)),
                DISTANCE, FARE);
        LoginMember loginMember = new LoginMember(1L, EMAIL, AGE);

        given(authService.findMemberByToken("secrettokentoken")).willReturn(loginMember);
        given(pathService.findPath(eq(loginMember.getAge()), eq(SOURCE_STATION_ID), eq(TARGET_STATION_ID))).willReturn(pathResponse);

        mockMvc.perform(get("/paths?source=" + SOURCE_STATION_ID + "&target=" + TARGET_STATION_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer secrettokentoken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stations[*].name").value(
                        Matchers.containsInRelativeOrder("강남역", "잠실역", "삼전역")))
                .andDo(document("path-find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("stations.[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                                fieldWithPath("stations.[].name").type(JsonFieldType.STRING).description("역 이름"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
                        )
                ));
    }
}
