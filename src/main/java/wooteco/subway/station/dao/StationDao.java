package wooteco.subway.station.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = :id";
        namedParameterJdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

    public Optional<Station> findById(Long id) {
        String sql = "select * from STATION where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        final List<Station> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return result.stream().findAny();
    }

    public Optional<Station> findByName(String name) {
        String sql = "select * from STATION where name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        final List<Station> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return result.stream().findAny();
    }
}