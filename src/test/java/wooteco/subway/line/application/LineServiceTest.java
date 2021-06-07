package wooteco.subway.line.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.application.StationService;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineDao lineDao;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationService stationService;

    @InjectMocks
    private LineService lineService;

    @DisplayName("노선 저장 실패 - 구간의 상행역의 ID가 null")
    @Test
    void saveLine_fail_upStationIdIsNull() {
        // given
        LineRequest lineRequest = new LineRequest("이호선", "green lighten-1", null, 1L, 10, 100);
        given(lineDao.insert(any(Line.class))).willReturn(any(Line.class));

        // when, then
        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("추가하려는 구간의 상행역과 하행역에 null이 있습니다");

        then(stationService).should(never()).findExistentStationById(anyLong());
        then(sectionDao).should(never()).insert(any(), any());
    }

    @DisplayName("노선 저장 실패 - 구간의 하행역의 ID가 null")
    @Test
    void saveLine_fail_downStationIdIsNull() {
        // given
        LineRequest lineRequest = new LineRequest("이호선", "green lighten-1", 1L, null, 10, 100);
        given(lineDao.insert(any(Line.class))).willReturn(any(Line.class));

        // when, then
        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("추가하려는 구간의 상행역과 하행역에 null이 있습니다");

        then(stationService).should(never()).findExistentStationById(anyLong());
        then(sectionDao).should(never()).insert(any(), any());
    }

    @DisplayName("노선 저장 실패 - 구간의 상행역과 하행역의 ID가 같음")
    @Test
    void saveLine_fail_sameStationIds() {
        // given
        LineRequest lineRequest = new LineRequest("이호선", "green lighten-1", 1L, 1L, 10, 100);
        given(lineDao.insert(any(Line.class))).willReturn(any(Line.class));

        // when, then
        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("추가하려는 구간의 상행역과 하행역이 같습니다");

        then(stationService).should(never()).findExistentStationById(anyLong());
        then(sectionDao).should(never()).insert(any(), any());
    }
}
