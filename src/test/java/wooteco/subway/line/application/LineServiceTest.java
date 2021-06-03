package wooteco.subway.line.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.station.application.StationService;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {
    private static final Long UP_STATION_ID = 1L;
    private static final Long DOWN_STATION_ID = 2L;

    @Mock
    private LineService lineService;

    @Mock
    private LineDao lineDao;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationService stationService;

    @DisplayName("노선을 추가한다.")
    @Test
    void insert() throws Exception {
        LineRequest lineRequest = new LineRequest("1호선", "bg-red-600", UP_STATION_ID, DOWN_STATION_ID, 10);

    }
}
