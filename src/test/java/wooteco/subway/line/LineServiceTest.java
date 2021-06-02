package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.UpdateLineRequest;
import wooteco.subway.section.application.SectionService;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {

    private final long UPSTATION_ID = 1;
    private final long DOWNSTATION_ID = 2;
    private final int DISTANCE = 10;

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
        long id = 1;
        String name = "1호선";
        String color = "파란색";

        when(mockLineDao.insert(any(Line.class))).thenReturn(new Line(id, name, color));
        when(mockSectionService.saveByLineCreate(any(Line.class), any())).thenReturn(null);

        // when
        LineRequest request = new LineRequest(name, color, UPSTATION_ID, DOWNSTATION_ID, DISTANCE);
        LineResponse response = lineService.createLine(request);

        // then
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("전체 노선 반환")
    void showAllLines() {
        // given
        List<Line> lineList = Arrays.asList(
            new Line((long) 1, "1호선", "파란색"),
            new Line((long) 2, "2호선", "초록색"),
            new Line((long) 3, "3호선", "주황색")
        );

        when(mockLineDao.findAll()).thenReturn(lineList);

        // when
        List<Line> lines = lineService.findAll();

        // then
        assertThat(lines.get(0).getId()).isEqualTo(lines.get(0).getId());
        assertThat(lines.get(0).getName()).isEqualTo(lines.get(0).getName());
        assertThat(lines.get(0).getColor()).isEqualTo(lines.get(0).getColor());
        assertThat(lines.get(1).getId()).isEqualTo(lines.get(1).getId());
        assertThat(lines.get(1).getName()).isEqualTo(lines.get(1).getName());
        assertThat(lines.get(1).getColor()).isEqualTo(lines.get(1).getColor());
        assertThat(lines.get(2).getId()).isEqualTo(lines.get(2).getId());
        assertThat(lines.get(2).getName()).isEqualTo(lines.get(2).getName());
        assertThat(lines.get(2).getColor()).isEqualTo(lines.get(2).getColor());
    }

    @Test
    @DisplayName("ID를 통한 노선 정보 반환")
    void findOne() {
        // given
        Long id = 1L;
        String name = "1호선";
        String color = "파란색";

        when(mockLineDao.find(id)).thenReturn(new Line(id, name, color));

        // when
        LineResponse response = lineService.findOne(id);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("특정 노선 업데이트")
    void update() {
        // given
        long id = 1;
        String updateName = "7호선";
        String updateColor = "녹담색";

        when(mockLineDao.update(eq(id), any(Line.class))).thenReturn(1);
        when(mockLineDao.find(id)).thenReturn(new Line(id, updateName, updateColor));

        // when
        lineService.update(id, new UpdateLineRequest(updateName, updateColor));

        // then
        LineResponse response = lineService.findOne(id);
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo(updateName);
        assertThat(response.getColor()).isEqualTo("녹담색");
    }

    @Test
    @DisplayName("특정 노선 삭제")
    void delete() {
        // given
        long id = 1;
        String name = "1호선";
        String color = "파란색";

        when(mockLineDao.delete(id)).thenReturn(1);
        when(mockLineDao.find(id)).thenThrow(NoSuchLineException.class);

        //when
        lineService.delete(id);

        // then
        assertThatThrownBy(() -> lineService.findOne(id))
            .isInstanceOf(NoSuchLineException.class);
    }
}
