package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.exception.NoSuchLineException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineMapResponse;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.line.dto.UpdateLineRequest;
import wooteco.subway.section.application.SectionService;
import wooteco.subway.section.dto.SectionResponse;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {

    private final long UP_STATION_ID = 1;
    private final long DOWN_STATION_ID = 2;
    private final int DISTANCE = 10;
    private final int EXTRA_FARE = 0;

    @Mock
    private LineDao mockLineDao;
    @Mock
    private SectionService mockSectionService;
    @InjectMocks
    private LineService lineService;

    @Test
    @DisplayName("노선 만들기")
    void createLine() {
        // given
        Long id = 1L;
        String name = "1호선";
        String color = "파란색";
        int extraFare = 0;

        when(mockLineDao.insert(any(Line.class))).thenReturn(new Line(id, name, color, extraFare));
        when(mockSectionService.saveByLineCreate(any(Line.class), any())).thenReturn(null);

        // when
        LineRequest request = new LineRequest(name, color, UP_STATION_ID, DOWN_STATION_ID, DISTANCE, EXTRA_FARE);
        LineResponse response = lineService.createLine(request);

        // then
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getColor()).isEqualTo(color);
        assertThat(response.getExtraFare()).isEqualTo(extraFare);
    }

    @Test
    @DisplayName("전체 노선 반환")
    void showAllLines() {
        // given
        List<Line> lineList = Arrays.asList(
            new Line(1L, "1호선", "파란색", 1_000),
            new Line(2L, "2호선", "초록색", 3_000),
            new Line(3L, "3호선", "주황색", 500)
        );

        when(mockLineDao.findAll()).thenReturn(lineList);

        // when
        List<Line> lines = lineService.findAll();

        // then
        assertThat(lines.get(0).getId()).isEqualTo(lines.get(0).getId());
        assertThat(lines.get(0).getName()).isEqualTo(lines.get(0).getName());
        assertThat(lines.get(0).getColor()).isEqualTo(lines.get(0).getColor());
        assertThat(lines.get(0).getExtraFare()).isEqualTo(lines.get(0).getExtraFare());
        assertThat(lines.get(1).getId()).isEqualTo(lines.get(1).getId());
        assertThat(lines.get(1).getName()).isEqualTo(lines.get(1).getName());
        assertThat(lines.get(1).getColor()).isEqualTo(lines.get(1).getColor());
        assertThat(lines.get(1).getExtraFare()).isEqualTo(lines.get(1).getExtraFare());
        assertThat(lines.get(2).getId()).isEqualTo(lines.get(2).getId());
        assertThat(lines.get(2).getName()).isEqualTo(lines.get(2).getName());
        assertThat(lines.get(2).getColor()).isEqualTo(lines.get(2).getColor());
        assertThat(lines.get(2).getExtraFare()).isEqualTo(lines.get(2).getExtraFare());
    }

    @Test
    @DisplayName("ID를 통한 노선 정보 반환")
    void findOne() {
        // given
        Long id = 1L;
        String name = "1호선";
        String color = "파란색";
        int extraFare = 1_000;

        when(mockLineDao.find(id)).thenReturn(new Line(id, name, color, extraFare));

        // when
        LineResponse response = lineService.findOne(id);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getColor()).isEqualTo(color);
        assertThat(response.getExtraFare()).isEqualTo(extraFare);
    }

    @Test
    @DisplayName("특정 노선 업데이트")
    void update() {
        // given
        Long id = 1L;
        String updateName = "7호선";
        String updateColor = "녹담색";
        int extraFare = 2_000;

        when(mockLineDao.update(eq(id), any(Line.class))).thenReturn(1);
        when(mockLineDao.find(id)).thenReturn(new Line(id, updateName, updateColor, extraFare));

        // when
        lineService.update(id, new UpdateLineRequest(updateName, updateColor, extraFare));

        // then
        LineResponse response = lineService.findOne(id);
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo(updateName);
        assertThat(response.getColor()).isEqualTo("녹담색");
        assertThat(response.getExtraFare()).isEqualTo(2_000);
    }

    @Test
    @DisplayName("특정 노선 삭제")
    void delete() {
        // given
        Long id = 1L;
        when(mockLineDao.delete(id)).thenReturn(1);
        when(mockLineDao.find(id)).thenThrow(NoSuchLineException.class);

        //when
        lineService.delete(id);

        // then
        assertThatThrownBy(() -> lineService.findOne(id))
            .isInstanceOf(NoSuchLineException.class);
    }

    @Test
    @DisplayName("전체 지하철 지도를 위한 전체 노선 정보값 반환")
    void findLineMapResponses() {
        // given
        Line 일호선 = new Line(1L, "1호선", "bg-blue-100", 1_000);
        Line 이호선 = new Line(2L, "2호선", "bg-green-100", 2_000);
        List<Line> lines = Arrays.asList(일호선, 이호선);

        List<TransferLineResponse> transferLineResponses = Collections.singletonList(TransferLineResponse.from(이호선));
        List<SectionResponse> sectionResponses = Arrays.asList(
            new SectionResponse(1L, "면목역", 10, transferLineResponses),
            new SectionResponse(2L, "상봉역", 15, transferLineResponses),
            new SectionResponse(3L, "중화역", 0, transferLineResponses)
        );

        when(mockLineDao.findAll()).thenReturn(lines);
        when(mockSectionService.findSectionResponses(일호선)).thenReturn(sectionResponses);

        // when
        List<LineMapResponse> lineMapResponses = lineService.findLineMapResponses();

        // then
        assertThat(lineMapResponses.get(0).getName()).isEqualTo(일호선.getName());
        assertThat(lineMapResponses.get(0).getColor()).isEqualTo(일호선.getColor());
        assertThat(lineMapResponses.get(0).getDistance()).isEqualTo(25);
        assertThat(lineMapResponses.get(0).getExtraFare()).isEqualTo(1_000);
        assertThat(lineMapResponses.get(0).getSections().get(0).getTransferLines().get(0).getName()).isEqualTo(이호선.getName());
        assertThat(lineMapResponses.get(0).getSections().get(0).getTransferLines().get(0).getColor()).isEqualTo(이호선.getColor());
    }
}
