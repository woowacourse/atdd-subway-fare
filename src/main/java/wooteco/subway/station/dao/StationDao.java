package wooteco.subway.station.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.notfound.StationNotFoundException;
import wooteco.subway.line.dto.LineNameColorResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationTransferLinesDto;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    private SqlParameterSource parameterSource(Station station) {
        return new BeanPropertySqlParameterSource(station);
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from station";
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "delete from station where id = :id";
        Map<String, Long> param = Collections.singletonMap("id", id);
        int count = namedParameterJdbcTemplate.update(sql, param);
        if (count < 1) {
            throw new StationNotFoundException();
        }
    }

    public Optional<Station> findById(Long id) {
        try {
            String sql = "select * from station where id = :id";
            Map<String, Long> param = Collections.singletonMap("id", id);
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Station station) {
        String sql = "update station set name = :name where id = :id";
        int count = namedParameterJdbcTemplate.update(sql, parameterSource(station));
        if (count < 1) {
            throw new StationNotFoundException();
        }
    }

    public List<StationTransferLinesDto> findAllWithTransferLines() {
        String sql = "SELECT DISTINCT st.id, st.name, l.id AS line_id, l.name AS line_name, l.color AS line_color " +
                "FROM station AS st " +
                "LEFT JOIN section AS sec ON st.id = sec.up_station_id OR st.id = sec.down_station_id " +
                "LEFT JOIN line AS l ON sec.line_id = l.id";
        try {
            return jdbcTemplate.queryForObject(sql, stationRowMapperWithTransferLines);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private final RowMapper<List<StationTransferLinesDto>> stationRowMapperWithTransferLines = (rs, rowNum) -> {
        List<StationTransferLinesDto> stationTransferLinesDtos = new ArrayList<>();
        Long stationId = 0L;
        while (!stationId.equals(rs.getLong("id")) || !rs.isLast()) {
            stationId = rs.getLong("id");
            String stationName = rs.getString("name");
            List<LineNameColorResponse> lineNameColorResponses = extractLine(rs, stationId);
            stationTransferLinesDtos.add(new StationTransferLinesDto(stationId, stationName, lineNameColorResponses));
        }
        return stationTransferLinesDtos;
    };

    private List<LineNameColorResponse> extractLine(ResultSet result, Long stationId) throws SQLException {
        List<LineNameColorResponse> lineNameColorResponses = new ArrayList<>();
        while (!result.isLast() && result.next() && stationId.equals(result.getLong("id"))) {
            lineNameColorResponses.add(
                    new LineNameColorResponse(
                            result.getLong("line_id"),
                            result.getString("line_name"),
                            result.getString("line_color"))
            );
        }
        return lineNameColorResponses;
    }
}