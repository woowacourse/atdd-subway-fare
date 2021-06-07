package wooteco.subway.line.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.exception.application.NonexistentTargetException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@ActiveProfiles("test")
@DataJdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;
    private LineDao lineDao;
    private SectionDao sectionDao;
    private Station 서초, 교대, 강남, 역삼;
    private Section 서초_교대, 교대_강남;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        initializeDummyData();
        서초_교대 = sectionDao.insert(이호선, new Section(서초, 교대, 10));
        교대_강남 = sectionDao.insert(이호선, new Section(교대, 강남, 5));
    }

    @DisplayName("구간 하나 생성")
    @Test
    void insert() {
        // when
        Section 강남_역삼 = sectionDao.insert(이호선, new Section(강남, 역삼, 3));

        // then
        assertThat(강남_역삼.getUpStation()).isEqualTo(강남);
        assertThat(강남_역삼.getDownStation()).isEqualTo(역삼);
        assertThat(강남_역삼.getDistance()).isEqualTo(3);
    }

    @DisplayName("노선ID로 모든 구간 삭제")
    @Test
    void deleteByLineId() {
        // when, then
        assertThatCode(() -> sectionDao.deleteByLineId(이호선.getId()))
            .doesNotThrowAnyException();
    }

    @DisplayName("노선ID로 모든 구간 삭제 실패 - 해당 ID의 노선이 없음")
    @Test
    void deleteByLineId_fail_nonexistentLine() {
        // when, then
        assertThatThrownBy(() -> sectionDao.deleteByLineId(1000L))
            .isInstanceOf(NonexistentTargetException.class)
            .hasMessageContaining("대상이 존재하지 않습니다");
    }

    private void initializeDummyData() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        서초 = stationDao.insert(new Station("서초역"));
        교대 = stationDao.insert(new Station("교대역"));
        강남 = stationDao.insert(new Station("강남역"));
        역삼 = stationDao.insert(new Station("역심역"));
        이호선 = lineDao.insert(new Line("이호선", "green lighten-1", 200));
    }
}
