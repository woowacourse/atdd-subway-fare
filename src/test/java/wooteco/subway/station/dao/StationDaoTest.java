package wooteco.subway.station.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StationDaoTest {

    private static final String NAME1 = "1역";
    private static final String NAME2 = "2역";
    private static final String NAME3 = "3역";
    private static final String NOT_EXIST_NAME = "뷁";

    StationDao stationDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    private void insertDummyData() {
        Station station1 = new Station(NAME1);
        Station station2 = new Station(NAME2);
        Station station3 = new Station(NAME3);
        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);
    }

    @Test
    @DisplayName("역을 삽입한다.")
    void insert() {
        Station station = new Station(NAME1);
        final Station insertStation = stationDao.insert(station);
        assertThat(insertStation.getName()).isEqualTo(NAME1);
    }

    @Test
    @DisplayName("모든 역을 조회한다.")
    void findAll() {
        insertDummyData();
        final List<Station> stations = stationDao.findAll();
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.stream().map(Station::getName).collect(Collectors.toList()))
                .contains(NAME1, NAME2, NAME3);
    }

    @Test
    @DisplayName("Id로 역을 삭제한다.")
    void deleteById() {
        insertDummyData();

        assertThatCode(() -> stationDao.deleteById(1L)).doesNotThrowAnyException();
        assertThat(stationDao.isExistById(1L)).isFalse();
    }

    @Test
    @DisplayName("Id로 역을 조회한다.")
    void findById() {
        insertDummyData();
        final Station station = stationDao.findById(1L);

        assertThat(station.getId()).isEqualTo(1L);
        assertThat(station.getName()).isEqualTo(NAME1);
    }

    @Test
    @DisplayName("이름으로 역이 존재하는지 확인한다.")
    void isExistByName() {
        insertDummyData();
        assertThat(stationDao.isExistByName(NAME1)).isTrue();
        assertThat(stationDao.isExistByName(NOT_EXIST_NAME)).isFalse();
    }

    @Test
    @DisplayName("Id로 역이 존재하는지 확인한다.")
    void isExistById() {
        insertDummyData();
        assertThat(stationDao.isExistById(1L)).isTrue();
        assertThat(stationDao.isExistById(100L)).isFalse();
    }
}