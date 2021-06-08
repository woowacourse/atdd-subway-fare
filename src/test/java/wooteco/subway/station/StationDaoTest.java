package wooteco.subway.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.exception.DuplicatedStationNameException;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(StationDao.class)
@ActiveProfiles("test")
public class StationDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    StationDao stationDao;

    @Test
    @DisplayName("정상적인 역 삽입")
    public void stationInsert() {
        stationDao.insert(new Station("kang"));
        String stationName = jdbcTemplate.queryForObject("select name from STATION where name = 'kang'", String.class);
        assertThat(stationName).isEqualTo("kang");
    }

    @Test
    @DisplayName("아무것도 들어있지 않을 때의 역 전체 조회")
    public void findAllNothing() {
        List<Station> stations = stationDao.findAll();
        assertThat(stations).isEmpty();
    }

    @Test
    @DisplayName("역 전체 조회")
    public void findAll() {
        //given
        List<Station> givenStations = Arrays.asList(new Station("a"), new Station("b"), new Station("c"));

        //when
        insert("a");
        insert("b");
        insert("c");

        //then
        List<Station> stations = stationDao.findAll();
        assertThat(stations).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(givenStations);
    }

    @Test
    @DisplayName("역 삭제")
    public void deleteById() {
        //given
        long id = insert("a");
        Boolean beforeDeleteResult = jdbcTemplate.queryForObject(
                "select exists (select * from STATION where id = ?)", Boolean.class, id);
        assertThat(beforeDeleteResult).isTrue();

        //when
        stationDao.deleteById(id);

        //then
        Boolean afterDeleteResult = jdbcTemplate.queryForObject(
                "select exists (select * from STATION where id = ?)", Boolean.class, id);
        assertThat(afterDeleteResult).isFalse();
    }

    @Test
    @DisplayName("id로 역 조회")
    public void findById() {
        //given
        long id = insert("a");

        //when
        String stationName = jdbcTemplate.queryForObject("select name from STATION where id = ?", String.class, id);

        //then
        assertThat(stationName).isEqualTo("a");
    }

    private long insert(String stationName) {
        String sql = "insert into STATION (name) values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, stationName);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
