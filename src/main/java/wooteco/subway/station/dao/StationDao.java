package wooteco.subway.station.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.dto.LineNameColorDto;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationTransferLinesDto;

import javax.sql.DataSource;
import java.util.ArrayList;
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


    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
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

    public Optional<Station> findById(Long id) {
        try {
            String sql = "select * from station where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Station station) {
        String sql = "update station set name = ? where id = ?";
        jdbcTemplate.update(sql, station.getName(), station.getId());
    }

    public List<StationTransferLinesDto> findAllWithTransferLines() {
        String sql = "SELECT DISTINCT st.id, st.name, l.id AS line_id, l.name AS line_name, l.color AS line_color " +
                "FROM station AS st " +
                "JOIN section AS sec ON st.id = sec.up_station_id OR st.id = sec.down_station_id " +
                "JOIN line AS l ON sec.line_id = l.id";
        return jdbcTemplate.queryForObject(sql, stationRowMapperWithTransferLines);
    }

    private RowMapper<List<StationTransferLinesDto>> stationRowMapperWithTransferLines = (rs, rowNum) -> {
        List<StationTransferLinesDto> stationTransferLinesDtos = new ArrayList<>();
        Long stationId = 0L;
        while (!stationId.equals(rs.getLong("id")) || !rs.isLast()) {
            stationId = rs.getLong("id");
            String stationName = rs.getString("name");
            List<LineNameColorDto> lineNameColorDtos = new ArrayList<>();
            do {
                lineNameColorDtos.add(
                        new LineNameColorDto(
                                rs.getLong("line_id"),
                                rs.getString("line_name"),
                                rs.getString("line_color"))
                );
            } while (!rs.isLast() && rs.next() && stationId.equals(rs.getLong("id")));
            stationTransferLinesDtos.add(new StationTransferLinesDto(stationId, stationName, lineNameColorDtos));
        }

        return stationTransferLinesDtos;
    };
}