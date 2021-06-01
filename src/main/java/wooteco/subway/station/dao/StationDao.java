package wooteco.subway.station.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationWithTransferResponse;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StationDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("STATION")
                .usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION order by id desc";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Station findById(Long id) {
        String sql = "select * from STATION where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public boolean isExistByName(String name) {
        String sql = "select EXISTS (select * from STATION where name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    public boolean isExistById(Long id) {
        String sql = "select EXISTS (select * from STATION where id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public List<StationWithTransferResponse> findAllStationWithTransfer() {
        String sql = "select S.id AS station_id, S.name AS station_name, L.id AS line_id, L.name AS line_name " +
                "FROM STATION AS S " +
                "RIGHT JOIN SECTION ON (SECTION.up_station_id = S.id OR SECTION.down_station_id = S.id) " +
                "LEFT JOIN LINE AS L ON L.id = SECTION.line_id";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<Long, List<Map<String, Object>>> resultByStationId = result.stream()
                .collect(Collectors.groupingBy(it -> (Long) it.get("station_id")));

        return resultByStationId.values()
                .stream()
                .map(this::makeStationWithTransferResponse)
                .collect(Collectors.toList());
    }

    private StationWithTransferResponse makeStationWithTransferResponse(final List<Map<String, Object>> result) {
        List<String> lineNames = extractTransferableLineNames(result);

        return new StationWithTransferResponse(
                (Long) result.get(0).get("station_id"),
                (String) result.get(0).get("station_name"),
                lineNames
        );
    }

    private List<String> extractTransferableLineNames(List<Map<String, Object>> result) {
        if (result.isEmpty() || result.get(0).get("line_id") == null) {
            return Collections.EMPTY_LIST;
        }

        return result.stream()
                .collect(Collectors.groupingBy(it -> it.get("line_id")))
                .entrySet()
                .stream()
                .map(it -> (String) it.getValue().get(0).get("line_name"))
                .collect(Collectors.toList());
    }
}