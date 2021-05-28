package wooteco.subway.station.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper;


    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;

        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");

        this.rowMapper = (rs, rowNum) ->
                new Station(
                        rs.getLong("id"),
                        rs.getString("name")
                );
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();

        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "delete from station where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Station findById(Long id) {
        String sql = "select * from station where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void updateStation(Station station) {
        String sql = "update station set name = ? where id = ?";
        jdbcTemplate.update(sql, station.getName(), station.getId());
    }

    public boolean existsByNameExceptId(Long id, String name) {
        String sql = "select exists (select * from station where id <> ? and name = ?)";

        return jdbcTemplate.queryForObject(sql, boolean.class, id, name);
    }

    public boolean existsById(Long id) {
        String sql = "select exists (select * from station where id = ?)";

        return jdbcTemplate.queryForObject(sql, boolean.class, id);
    }
}