package wooteco.subway.station.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import wooteco.subway.exception.notfound.StationNotFoundException;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StationDao 관련")
@DataJdbcTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StationDaoTest {
    private Station station1;
    private Station station2;
    private StationDao stationDao;

    public StationDaoTest(DataSource dataSource) {
        stationDao = new StationDao(new JdbcTemplate(dataSource), dataSource);
    }

    @BeforeEach
    void setUp() {
        station1 = new Station("강남역");
        station2 = new Station("역삼역");
    }

    @DisplayName("지하철 역 저장한다.")
    @Test
    void insert() {
        //when
        Station insertStation = stationDao.insert(station1);

        //then
        assertThat(insertStation.getName()).isEqualTo(station1.getName());
    }

    @DisplayName("중복되는 이름으로 지하철 역 저장한다.")
    @Test
    void insertByDuplicateName() {
        //given
        stationDao.insert(station1);

        //when then
        assertThatThrownBy(() -> stationDao.insert(station1))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 역을 조회한다.")
    @Test
    void findAll() {
        //given
        List<Station> beforeInsertStations = stationDao.findAll();
        stationDao.insert(station1);
        stationDao.insert(station2);

        //when
        List<Station> afterInsertStations = stationDao.findAll();

        //then
        assertThat(beforeInsertStations).hasSize(0);
        assertThat(afterInsertStations).hasSize(2);
    }

    @DisplayName("지하철 역 ID로 지하철 역을 삭제한다.")
    @Test
    void deleteById() {
        //given
        Station insertStation = stationDao.insert(station1);
        Long savedStationId = insertStation.getId();

        //when
        stationDao.deleteById(savedStationId);
        Optional<Station> findStation = stationDao.findById(savedStationId);

        //then
        assertThat(findStation).isEqualTo(Optional.empty());
    }

    @DisplayName("지하철 역 ID로 지하철 역을 삭제한다.")
    @Test
    void deleteByNotExistId() {
        //when then
        assertThatThrownBy(() -> stationDao.deleteById(Long.MAX_VALUE))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    void findById() {
        //
    }

    @Test
    void update() {
    }

    @Test
    void findAllWithTransferLines() {
    }
}