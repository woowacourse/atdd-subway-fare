package wooteco.subway.station.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class StationDaoTest {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(namedParameterJdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("DB에 역 정보를 저장한다.")
    void insert() {
        final Station 테스트역 = stationDao.insert(new Station("테스트역"));
        assertThat(테스트역.getId()).isEqualTo(1L);
        assertThat(테스트역.getName()).isEqualTo("테스트역");
    }

    @Test
    @DisplayName("DB에 저장한 역 정보를 가져온다.")
    void findAll() {
        final Station 테스트역1 = stationDao.insert(new Station("테스트역1"));
        final Station 테스트역2 = stationDao.insert(new Station("테스트역2"));
        final Station 테스트역3 = stationDao.insert(new Station("테스트역3"));

        final List<Station> stations = stationDao.findAll();
        final List<String> stationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactly(테스트역1.getName(), 테스트역2.getName(), 테스트역3.getName());
    }

    @Test
    @DisplayName("DB에 저장한 역을 id로 삭제한다")
    void deleteById() {
        final Station 테스트역 = stationDao.insert(new Station("테스트역"));
        stationDao.deleteById(테스트역.getId());

        final Optional<Station> stationById = stationDao.findById(테스트역.getId());
        assertThat(stationById.isPresent()).isFalse();
    }

    @Test
    @DisplayName("id로 DB에 저장된 역을 가져온다")
    void findById() {
        final Station 테스트역 = stationDao.insert(new Station("테스트역"));
        final Optional<Station> stationById = stationDao.findById(테스트역.getId());

        assertThat(stationById.get().getName()).isEqualTo("테스트역");
        assertThat(stationById.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("name으로 DB에 저장된 역을 가져온다")
    void findByName() {
        final Station 테스트역 = stationDao.insert(new Station("테스트역"));
        final Optional<Station> stationById = stationDao.findByName(테스트역.getName());

        assertThat(stationById.get().getName()).isEqualTo("테스트역");
        assertThat(stationById.get().getId()).isEqualTo(1L);
    }
}