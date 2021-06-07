package wooteco.subway.station.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.exception.application.NonexistentTargetException;
import wooteco.subway.station.dao.StationDao;

@ActiveProfiles("test")
@DataJdbcTest
class StationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationService stationService;

    @BeforeEach
    void setUp() {
        stationService = new StationService(
            new StationDao(jdbcTemplate, dataSource)
        );
    }

    @DisplayName("역 조회 실패 - 역이 존재하지 않음")
    @Test
    void findExistentStationById_fail_nonexistentStation() {
        assertThatThrownBy(() -> stationService.findExistentStationById(1000L))
            .isInstanceOf(NonexistentTargetException.class)
            .hasMessageContaining("대상이 존재하지 않습니다");
    }
}
