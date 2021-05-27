package wooteco.subway.station.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("STATION")
                .usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Station> findById(Long id) {
        String sql = "select * from STATION where id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findAny();
    }

    public Optional<Station> findByName(String name) {
        String sql = "select * from STATION where name = ? ";
        return jdbcTemplate.query(sql, rowMapper, name)
                .stream()
                .findAny();
    }

    public void update(String newName, Long id) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, newName, id);
    }

    public int countRegisteredStations(Long id) {
        String sql = "select count(*) from SECTION where ? in (up_station_id, down_station_id)";
        return jdbcTemplate.queryForObject(sql, int.class, id);
    }
}
