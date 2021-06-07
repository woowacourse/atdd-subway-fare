package wooteco.subway.line.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.exception.application.DuplicatedFieldException;
import wooteco.subway.exception.application.NonexistentTargetException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.exception.LineCompositionException;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@ActiveProfiles("test")
@DataJdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private Station 서초, 교대, 강남, 고터;
    private Section 서초_교대, 교대_강남;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        이호선 = lineDao.insert(new Line("이호선", "green lighten-1", 200));
        initializeDummyData();
        이호선.addSection(서초_교대);
        이호선.addSection(교대_강남);
    }

    @DisplayName("노선 하나 생성")
    @Test
    void insert() {
        // given
        Line 삼호선 = new Line("삼호선", "orange lighten-1", 300);

        // when
        Line line = lineDao.insert(삼호선);

        // then
        assertThat(line).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(삼호선);
    }

    @DisplayName("노선 생성에 실패 - 기존에 존재하는 노선 이름")
    @Test
    void insert_fail_duplicatedName() {
        // given
        Line duplicatedNameLine = new Line("이호선", "blue lighten-1", 100);

        // when, then
        assertThatThrownBy(() -> lineDao.insert(duplicatedNameLine))
            .isInstanceOf(DuplicatedFieldException.class)
            .hasMessageContaining("값이 중복되었습니다");
    }

    @DisplayName("ID로 노선 하나 조회")
    @Test
    void findById() {
        // when
        Line line = lineDao.findById(이호선.getId());

        // then
        assertThat(line).usingRecursiveComparison()
            .isEqualTo(이호선);
    }

    @DisplayName("ID로 노선 하나 조회 - 없는 ID인 경우")
    @Test
    void findById_fail_nonexistentId() {
        // when, then
        assertThatThrownBy(() -> lineDao.findById(1000L))
            .isInstanceOf(LineCompositionException.class)
            .hasMessageContaining("노선 데이터 생성에 필요한 데이터를 찾지 못했습니다");
    }

    @DisplayName("전체 노선 조회")
    @Test
    void findAll() {
        // when
        List<Line> lines = lineDao.findAll();

        // then
        assertThat(lines).extracting("id")
            .containsExactly(이호선.getId());
    }

    @DisplayName("노선 수정")
    @Test
    void update() {
        // given
        Line 수정될_이호선 = new Line(이호선.getId(), 이호선.getName(), "blue lighten-1", 100);

        // when
        lineDao.update(수정될_이호선);

        // then
        Line 수정후_이호선 = lineDao.findById(이호선.getId());
        assertThat(수정후_이호선.getColor()).isEqualTo(수정될_이호선.getColor());
        assertThat(수정후_이호선.getExtraFare()).isEqualTo(수정될_이호선.getExtraFare());
    }

    @DisplayName("노선 수정 실패 - 이미 존재하는 노선 이름")
    @Test
    void update_fail_duplicatedName() {
        // given
        Line 삼호선 = lineDao.insert(new Line("삼호선", "orange lighten-1", 300));
        Line 수정실패할_삼호선 = new Line(삼호선.getId(), "이호선", 삼호선.getColor(), 삼호선.getExtraFare());

        // when, then
        assertThatThrownBy(() -> lineDao.update(수정실패할_삼호선))
            .isInstanceOf(DuplicatedFieldException.class)
            .hasMessageContaining("값이 중복되었습니다");
    }

    @DisplayName("노선 수정 실패 - 없는 ID인 경우")
    @Test
    void update_fail_nonexistentId() {
        // given
        Line line = new Line(10000L, "육호선", "brown lighten-1", 600);

        // when, then
        assertThatThrownBy(() -> lineDao.update(line))
            .isInstanceOf(NonexistentTargetException.class)
            .hasMessageContaining("대상이 존재하지 않습니다");
    }

    @DisplayName("ID로 노선 삭제")
    @Test
    void deleteById() {
        // when
        lineDao.deleteById(이호선.getId());

        // then
        assertThatThrownBy(() -> lineDao.findById(이호선.getId()))
            .isInstanceOf(LineCompositionException.class);
    }

    @DisplayName("ID로 노선 삭제 실패 - 없는 ID인 경우")
    @Test
    void deleteById_fail_nonexistentId() {
        // when, then
        assertThatThrownBy(() -> lineDao.deleteById(10000L))
            .isInstanceOf(NonexistentTargetException.class)
            .hasMessageContaining("대상이 존재하지 않습니다");
    }

    private void initializeDummyData() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        서초 = stationDao.insert(new Station("서초역"));
        교대 = stationDao.insert(new Station("교대역"));
        강남 = stationDao.insert(new Station("강남역"));
        고터 = stationDao.insert(new Station("고터역"));
        서초_교대 = sectionDao.insert(이호선, new Section(서초, 교대, 10));
        교대_강남 = sectionDao.insert(이호선, new Section(교대, 강남, 5));
    }
}
