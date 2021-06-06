package wooteco.subway.line.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class SectionDaoTest {

    @Autowired
    DataSource dataSource;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    private Station 테스트역1;
    private Station 테스트역2;
    private Station 테스트역3;
    private Line 테스트선;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(namedParameterJdbcTemplate, dataSource);
        stationDao = new StationDao(namedParameterJdbcTemplate, dataSource);
        lineDao = new LineDao(namedParameterJdbcTemplate, dataSource);

        테스트역1 = stationDao.insert(new Station("테스트역1"));
        테스트역2 = stationDao.insert(new Station("테스트역2"));
        테스트역3 = stationDao.insert(new Station("테스트역3"));

        테스트선 = lineDao.insert(new Line(1L, "노선명1", "노선색1", 100));
    }

    @Test
    @DisplayName("DB에 등록된 station들은 section으로 구간에 추가해줄 수 있다.")
    void insert() {
        final Section section = new Section(1L, 테스트역1, 테스트역2, 10);
        final Section sectionInserted = sectionDao.insert(테스트선, section);

        assertThat(sectionInserted.getId()).isEqualTo(1L);
        assertThat(sectionInserted.getUpStation().getName()).isEqualTo(테스트역1.getName());
        assertThat(sectionInserted.getDownStation().getName()).isEqualTo(테스트역2.getName());
        assertThat(sectionInserted.getDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("해당 Line에 속한 모든 구간을 삭제한다.")
    void deleteByLineId() {
        Section section = new Section(1L, 테스트역1, 테스트역2, 10);
        sectionDao.insert(테스트선, section);
        Optional<Line> lineById = lineDao.findById(테스트선.getId());
        assertThat(lineById.get().getSections().getSections()).hasSize(1);

        sectionDao.deleteByLineId(테스트선.getId());

        lineById = lineDao.findById(테스트선.getId());
        assertThat(lineById.get().getSections().getSections()).hasSize(0);
    }

    @Test
    @DisplayName("해당 Line에 속한 구간 전체를 추가한다.")
    void insertSections() {
        Section section = new Section(1L, 테스트역1, 테스트역2, 10);
        테스트선.addSection(section);
        sectionDao.insertSections(테스트선);

        Optional<Line> lineById = lineDao.findById(테스트선.getId());
        assertThat(lineById.get().getSections().getSections()).hasSize(1);
    }
}