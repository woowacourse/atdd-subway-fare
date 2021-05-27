package wooteco.subway.station.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.application.StationException;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private RowMapper<Line> lineInfoRowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("extra_fare")
            );

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("STATION")
                .usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params)
                .longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        try {
            String sql = "delete from STATION where id = ?";
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            throw new StationException("이미 구간에 포함된 역을 삭제할 수 없습니다.");
        }
    }

    public Optional<Station> findById(Long id) {
        try {
            String sql = "select * from STATION where id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isExistName(String name) {
        String sql = "SELECT EXISTS (SELECT id FROM STATION WHERE name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    public List<Line> findLinesPassing(Station station) {
        String sql = "SELECT DISTINCT L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as extra_fare FROM LINE L " +
                "left outer join SECTION S WHERE L.id = S.line_id AND (S.up_station_id =? OR S.down_station_id = ?)";
        return jdbcTemplate.query(sql, lineInfoRowMapper, station.getId(), station.getId());
    }

    public void updateById(Long id, String name) {
        String sql = "update station set name = ? where id = ?";
        jdbcTemplate.update(sql, name, id);
    }
}
