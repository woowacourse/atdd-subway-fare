package wooteco.subway.station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
public class StationDaoTest {
    private StationDao stationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        this.stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("중복되는 이름을 가지는 역을 추가할 시 DuplicateKeyException이 발생한다.")
    @Test
    void throw_DuplicateKeyException_When_Insert_NonExists() {
        stationDao.insert(new Station("잠실역"));
        assertThatThrownBy(() -> stationDao.insert(new Station("잠실역")))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("존재하지 않는 지하철 역을 검색할 시 EmptyResultDataAccessException이 발생한다.")
    @Test
    void throw_NoSuchElementException_When_Find_NonExists() {
        assertThatThrownBy(() -> stationDao.findById(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("존재하지 않는 지하철 역을 삭제할 시 affectedRow가 0이다.")
    @Test
    void throw_NoSuchElementException_When_Delete_NonExists() {
        assertThat(stationDao.deleteById(1L)).isEqualTo(0);
    }
}
