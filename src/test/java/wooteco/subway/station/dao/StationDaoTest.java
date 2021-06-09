package wooteco.subway.station.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import wooteco.subway.exception.application.ReferenceConstraintException;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

@ActiveProfiles("test")
@DataJdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    private Station 서초, 교대;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        서초 = stationDao.insert(new Station("서초역"));
        교대 = stationDao.insert(new Station("교대역"));
    }

    @DisplayName("역 생성에 성공")
    @Test
    void insert() {
        // when
        Station 강남 = stationDao.insert(new Station("강남역"));

        // then
        assertThat(강남.getName()).isEqualTo("강남역");
    }

    @DisplayName("역 생성에 실패 - 기존에 존재하는 역의 이름")
    @Test
    void insert_fail_duplicatedName() {
        // when, then
        assertThatThrownBy(() -> stationDao.insert(new Station("서초역")))
            .isInstanceOf(DuplicatedFieldException.class)
            .hasMessageContaining("값이 중복되었습니다");
    }

    @DisplayName("전체 역 조회")
    @Test
    void findAll() {
        // when
        List<Station> stations = stationDao.findAll();

        // then
        assertThat(stations).containsExactly(서초, 교대);
    }

    @DisplayName("ID로 역 하나 조회")
    @Test
    void findById() {
        // given
        Station 강남 = stationDao.insert(new Station("강남역"));
        Long 강남ID = 강남.getId();

        // when
        Optional<Station> optionalStation = stationDao.findById(강남ID);

        // then
        assertThat(optionalStation.get()).isEqualTo(강남);
    }

    @DisplayName("ID로 역 하나 조회 - 없는 ID인 경우")
    @Test
    void findById_nonexistentId() {
        // when
        final Optional<Station> optionalStation = stationDao.findById(10000L);

        // then
        assertThat(optionalStation).isEqualTo(Optional.empty());
    }

    @DisplayName("ID로 역 하나 삭제")
    @Test
    void deleteById() {
        // when
        stationDao.deleteById(서초.getId());

        // then
        assertThat(stationDao.findById(서초.getId()))
            .isEqualTo(Optional.empty());
    }

    @DisplayName("ID로 역 하나 삭제 실패 - 대상이 없음")
    @Test
    void deleteById_fail_nonexistentTarget() {
        // when, then
        assertThatThrownBy(() -> stationDao.deleteById(1000L))
            .isInstanceOf(NonexistentTargetException.class)
            .hasMessageContaining("대상이 존재하지 않습니다");
    }

    @DisplayName("ID로 역 하나 삭제 실패 - 해당역을 참조하는 부분이 존재")
    @Test
    void deleteById_fail_referenceExistent() {
        // given
        SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        Sections sections = new Sections(
            Collections.singletonList(new Section(서초, 교대, 10))
        );
        Line 이호선 = new Line(1L, "이호선", "green lighten-1", 200, sections);
        sectionDao.insertSections(이호선);

        // when, then
        assertThatThrownBy(() -> stationDao.deleteById(서초.getId()))
            .isInstanceOf(ReferenceConstraintException.class)
            .hasMessageContaining("해당역을 참조하는 부분이 있어 삭제할 수 없습니다");
    }
}
