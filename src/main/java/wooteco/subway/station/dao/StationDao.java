package wooteco.subway.station.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.dto.StationTransferResponse;
import wooteco.subway.line.dto.TransferLineResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationLinesResponse;

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

    public List<Station> findAll() {
        String sql = "select * from STATION";
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

    public boolean findExistingStationById(Long id) {
        String sql = "select exists (select count(*) from STATION where id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public boolean findStationByName(String name) {
        String sql = "select exists (select count(*) from STATION where name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    public void updateById(Long id, String name) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, name, id);
    }

    public List<StationTransferResponse> getStationsWithTransferLines(final long lineId) {
        String query = "SELECT S.id AS station_id," +
            "S.name AS station_name, " +
            "L.id AS line_id, " +
            "L.name AS line_name, " +
            "L.color AS line_color " +
            "FROM STATION AS S " +
            "INNER JOIN (SELECT line_id, up_station_id, down_station_id FROM SECTION WHERE line_id = ?) AS SE " +
            "ON (SE.up_station_id = S.id OR SE.down_station_id = S.id) " +
            "LEFT JOIN SECTION AS SE_L " +
            "ON (SE_L.up_station_id = S.id OR SE_L.down_station_id = S.id) " +
            "LEFT JOIN LINE AS L " +
            "ON SE_L.line_id = L.id";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query, lineId);

        Map<Long, List<Map<String, Object>>> resultByStation = result.stream()
            .collect(Collectors.groupingBy(it -> (Long) it.get("station_id")));

        return resultByStation.entrySet()
            .stream()
            .map(it -> mapStationTransferResponse(it.getValue(), lineId))
            .collect(Collectors.toList());
    }

    private StationTransferResponse mapStationTransferResponse(List<Map<String, Object>> result,
        long lineId) {
        if (result.size() == 0) {
            throw new RuntimeException();
        }

        List<TransferLineResponse> transferLineResponses = extractTransferLine(result, lineId);

        return new StationTransferResponse(
            (Long) result.get(0).get("station_id"),
            (String) result.get(0).get("station_name"),
            transferLineResponses
        );
    }

    private List<TransferLineResponse> extractTransferLine(List<Map<String, Object>> result,
        long lineId) {
        if (result.isEmpty() || result.get(0).get("line_id") == null) {
            return Collections.EMPTY_LIST;
        }

        return result.stream()
            .collect(Collectors.groupingBy(it -> it.get("line_id")))
            .entrySet()
            .stream()
            .filter(it -> (long) it.getKey() != lineId)
            .map(it ->
                new TransferLineResponse(
                    (Long) it.getKey(),
                    (String) it.getValue().get(0).get("line_name"),
                    (String) it.getValue().get(0).get("line_color")
                ))
            .collect(Collectors.toList());
    }
}
