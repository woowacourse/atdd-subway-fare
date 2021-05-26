package wooteco.subway.station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;

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

    @DisplayName("중복되는 이름을 가지는 역 추가 시 예외가 발생한다.")
    @Test
    void duplicateStationNameExceptionTest() {
        stationDao.insert(new Station("잠실역"));
        assertThatThrownBy(() -> stationDao.insert(new Station("잠실역")))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
