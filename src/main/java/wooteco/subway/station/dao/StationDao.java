package wooteco.subway.station.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.exception.DuplicatedStationNameException;
import wooteco.subway.station.exception.NoSuchStationException;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StationDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertAction;

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
        try {
            Long id = insertAction.executeAndReturnKey(params).longValue();
            return new Station(id, station.getName());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedStationNameException();
        }
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        if (jdbcTemplate.update(sql, id) == 0) {
            throw new NoSuchStationException();
        }
    }

    public Station findById(Long id) {
        String sql = "select * from STATION where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchStationException();
        }
    }
}