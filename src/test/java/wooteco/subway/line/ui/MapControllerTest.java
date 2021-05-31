package wooteco.subway.line.ui;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.line.ui.dto.SectionResponse;
import wooteco.subway.line.ui.dto.SectionsResponse;
import wooteco.subway.line.ui.dto.map.MapResponse;
import wooteco.subway.line.ui.dto.map.StationOfMapResponse;
import wooteco.subway.line.ui.dto.sectionsofline.LineWithTransferLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.SectionsOfLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.StationOfLineResponse;
import wooteco.subway.line.ui.dto.sectionsofline.TransferLineResponse;
import wooteco.subway.member.infrastructure.dao.MemberDao;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.docs.ApiDocumentUtils.getDocumentRequest;
import static wooteco.docs.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(controllers = {MapController.class})
@AutoConfigureRestDocs
class MapControllerTest {
    private static final Line 삼팔선 = new Line(
            2L,
            "삼팔선",
            "green",
            new Sections(Collections.singletonList(
                    new Section(
                            1L,
                            new Station(1L, "강남역"),
                            new Station(2L, "너역"),
                            10
                    )
            )), 1000);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @MockBean
    private AuthService authService;

    @Test
    void showMap() throws Exception {
        MapResponse mapResponse = createMapResponse();
        given(lineService.findMap()).willReturn(Collections.singletonList(
                mapResponse
        ));

        ResultActions perform = mockMvc.perform(get("/map"))
                .andExpect(status().isOk());

        perform.andDo(document("Show map",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[].id").type(NUMBER).description("노선 id"),
                        fieldWithPath("[].name").type(STRING).description("노선 이름"),
                        fieldWithPath("[].color").type(STRING).description("노선 색"),
                        fieldWithPath("[].stations").type(ARRAY).description("노선에 포함된 역"),
                        fieldWithPath("[].stations[].id").type(NUMBER).description("역 id"),
                        fieldWithPath("[].stations[].name").type(STRING).description("역 이름"),
                        fieldWithPath("[].stations[].distance").type(NUMBER).description("다음 역과의 거리"),
                        fieldWithPath("[].stations[].transferLines").type(ARRAY).description("환승 가능한 노선"),
                        fieldWithPath("[].stations[].transferLines[].id").type(NUMBER).description("노선 id"),
                        fieldWithPath("[].stations[].transferLines[].name").type(STRING).description("노선 이름"),
                        fieldWithPath("[].stations[].transferLines[].color").type(STRING).description("노선 색깔")
                )
        ));
    }

    private MapResponse createMapResponse() {
        return new MapResponse(
                        1L,
                        "red",
                        "rk",
                        Collections.singletonList(
                                new StationOfMapResponse(
                                        1L,
                                        "강남역",
                                        10,
                                        Collections.singletonList(
                                                new TransferLineResponse(삼팔선)
                                        )
                                )
                        )
                );
    }

}