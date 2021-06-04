package wooteco.subway.line.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineWithSectionsResponse;
import wooteco.subway.line.exception.LineNotFoundException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    private static final String NAME = "일호선";
    private static final String COLOR = "bg-red-100";
    private static final Long UP_STATION_ID = 1L;
    private static final Long DOWN_STATION_ID = 2L;
    private static final int DISTANCE = 10;
    private static final int EXTRA_FARE = 0;
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "잠남역";

    @InjectMocks
    private LineService lineService;

    @Mock
    private StationService stationService;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private LineDao lineDao;

    @Test
    @DisplayName("노선명, 색이 중복되지 않은 노선 삽입 요청이 들어왔을 때 성공한다.")
    void saveLine() {
        final LineRequest lineRequest = new LineRequest(NAME, COLOR, UP_STATION_ID,
                DOWN_STATION_ID, DISTANCE, EXTRA_FARE);
        given(lineDao.isExistByName(lineRequest.getName())).willReturn(false);
        given(lineDao.isExistByColor(lineRequest.getColor())).willReturn(false);
        given(lineDao.insert(ArgumentMatchers.any(Line.class))).willReturn(new Line(1L, NAME, COLOR, EXTRA_FARE));
        given(stationService.findStationById(UP_STATION_ID)).willReturn(new Station(UP_STATION_ID, STATION_NAME1));
        given(stationService.findStationById(DOWN_STATION_ID)).willReturn(new Station(DOWN_STATION_ID, STATION_NAME2));

        final LineResponse lineResponse = lineService.saveLine(lineRequest);

        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getName()).isEqualTo(NAME);
        assertThat(lineResponse.getColor()).isEqualTo(COLOR);
    }

    @Test
    @DisplayName("노선 명이 중복인 노선 삽입 요청이 들어왔을 때 실패한다.")
    void saveLineOfDuplicatedName() {
        final LineRequest lineRequest = new LineRequest(NAME, COLOR, UP_STATION_ID,
                DOWN_STATION_ID, DISTANCE, EXTRA_FARE);
        given(lineDao.isExistByName(lineRequest.getName())).willReturn(true);
        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
                .isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("색이 중복인 노선 삽입 요청이 들어왔을 때 실패한다.")
    void saveLineOfDuplicatedColor() {
        final LineRequest lineRequest = new LineRequest(NAME, COLOR, UP_STATION_ID,
                DOWN_STATION_ID, DISTANCE, EXTRA_FARE);
        given(lineDao.isExistByName(lineRequest.getName())).willReturn(false);
        given(lineDao.isExistByColor(lineRequest.getColor())).willReturn(true);
        assertThatThrownBy(() -> lineService.saveLine(lineRequest))
                .isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("저장된 모든 노선을 불러온다.")
    void findLineWithSectionsResponses() {
        final Line line1 = new Line(1L, NAME, COLOR, EXTRA_FARE);
        final Line line2 = new Line(2L, NAME, COLOR, EXTRA_FARE);
        given(lineDao.findAll()).willReturn(Arrays.asList(line1, line2));
        final List<LineWithSectionsResponse> lineWithSectionsResponses = lineService.findLineWithSectionsResponses();
        assertThat(lineWithSectionsResponses.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("존재하는 아이디의 노선을 가져올 수 있다.")
    void findLineResponseById() {
        given(lineDao.isNotExistById(1L)).willReturn(false);
        given(lineDao.findById(1L)).willReturn(new Line(1L, NAME, COLOR, EXTRA_FARE));

        final LineResponse lineResponse = lineService.findLineResponseById(1L);
        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getName()).isEqualTo(NAME);
        assertThat(lineResponse.getColor()).isEqualTo(COLOR);
    }

    @Test
    @DisplayName("존재하지 않는 아이디의 노선을 가져올 수 없다.")
    void findLineResponseByInvalidId() {
        given(lineDao.isNotExistById(1L)).willReturn(true);
        assertThatThrownBy(() -> lineService.findLineResponseById(1L))
                .isInstanceOf(LineNotFoundException.class);
    }


    @Test
    @DisplayName("존재하지 않는 아이디의 노선을 수정할 수 없다.")
    void updateLineWithInvalidId() {
        final LineRequest lineRequest = new LineRequest(NAME, COLOR, UP_STATION_ID,
                DOWN_STATION_ID, DISTANCE, EXTRA_FARE);
        given(lineDao.isNotExistById(1L)).willReturn(true);
        assertThatThrownBy(() -> lineService.updateLine(1L, lineRequest))
                .isInstanceOf(LineNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 아이디의 노선을 삭제할 수 없다.")
    void deleteLineByInvalidId() {
        given(lineDao.isNotExistById(1L)).willReturn(true);
        assertThatThrownBy(() -> lineService.deleteLineById(1L))
                .isInstanceOf(LineNotFoundException.class);
    }
}