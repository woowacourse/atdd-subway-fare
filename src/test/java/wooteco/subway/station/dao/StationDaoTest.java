package wooteco.subway.station.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.application.StationException;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StationDaoTest {
    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDao sectionDao;

    @Test
    @DisplayName("노선에 포함된 역 삭제시 에러 발생")
    void deleteStationByIdOfExisingLine() {
        Station station1 = stationDao.insert(new Station("station1"));
        Station station2 = stationDao.insert(new Station("station2"));

        Line insertedLine = lineDao.insert(new Line("line", "color", 0));

        sectionDao.insert(insertedLine, new Section(station1, station2, 5));

        assertThatThrownBy(() -> stationDao.deleteById(station1.getId())).isInstanceOf(StationException.class);
    }
}
