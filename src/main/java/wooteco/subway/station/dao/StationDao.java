package wooteco.subway.station.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.application.NoSuchStationException;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.domain.StationWithLines;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
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

    public List<StationWithLines> findAllWithLines() {
        String sql = "select S.id AS station_id, S.name AS station_name, L.id AS line_id, L.name AS line_name, L.color AS line_color " +
                "FROM STATION AS S " +
                "LEFT JOIN SECTION AS SE ON (SE.up_station_id = S.id OR SE.down_station_id = S.id) " +
                "LEFT JOIN LINE AS L ON L.id = SE.line_id";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<Long, List<Map<String, Object>>> resultByStation = result.stream()
                .collect(Collectors.groupingBy(it -> (Long) it.get("station_id")));

        return resultByStation.entrySet()
                .stream()
                .map(it -> mapStationLineResponse(it.getValue()))
                .collect(Collectors.toList());
    }

    private StationWithLines mapStationLineResponse(final List<Map<String, Object>> result) {
        if (result.isEmpty()) {
            throw new NoSuchStationException();
        }

        List<Line> lines = extractTransferLine(result);

        return new StationWithLines(
                (Long) result.get(0).get("station_id"),
                (String) result.get(0).get("station_name"),
                lines
        );
    }

    private List<Line> extractTransferLine(List<Map<String, Object>> result) {
        if (result.isEmpty() || result.get(0).get("line_id") == null) {
            return Collections.emptyList();
        }
        return result.stream()
                .collect(Collectors.groupingBy(it -> it.get("line_id")))
                .entrySet()
                .stream()
                .map(it ->
                        new Line(
                                (Long) it.getKey(),
                                (String) it.getValue().get(0).get("line_name"),
                                (String) it.getValue().get(0).get("line_color")
                        ))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Station> findById(Long id) {
        String sql = "select * from STATION where id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findAny();
    }

    public boolean doesNameExist(String name) {
        String query = "SELECT EXISTS (SELECT * FROM STATION WHERE name = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, name);
    }

    public void updateById(final Long id, final String name) {
        String query = "UPDATE STATION SET name = ? WHERE id = ?";
        jdbcTemplate.update(query, name, id);
    }
}