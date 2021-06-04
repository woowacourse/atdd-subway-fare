package wooteco.subway.section.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.section.domain.Distance;
import wooteco.subway.section.domain.Id;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.domain.Station;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Section> rowMapper = (rs, rowNum) -> {
        Long sectionId = rs.getLong("section_id");
        Long lineId = rs.getLong("line_id");
        String lineName = rs.getString("line_name");
        String lineColor = rs.getString("line_color");
        Long upStationId = rs.getLong("up_id");
        String upStationName = rs.getString("up_name");
        Long downStationId = rs.getLong("down_id");
        String downStationName = rs.getString("down_name");
        int distance = rs.getInt("distance");
        int extraFare = rs.getInt("extra_fare");

        return new Section(
            new Id(sectionId),
            new Line(lineId, lineName, lineColor, extraFare),
            new Station(upStationId, upStationName),
            new Station(downStationId, downStationName),
            new Distance(distance)
        );
    };

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
            .withTableName("SECTION")
            .usingGeneratedKeyColumns("id");
    }

    public Section insert(Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", section.getLineId());
        params.put("up_station_id", section.getUpStationId());
        params.put("down_station_id", section.getDownStationId());
        params.put("distance", section.getDistanceValue());

        Long key = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Section(key, section);
    }

    public List<Section> findAllByLineId(Long lineId) {
        String sql =
            "SELECT s.id AS section_id, LINE.id AS line_id, LINE.name AS line_name, LINE.color AS line_color, LINE.extra_fare AS extra_fare, "
                + "up_station.id AS up_id, up_station.name AS up_name, "
                + "down_station.id AS down_id, down_station.name AS down_name, "
                + "distance "
                + "FROM SECTION AS s "
                + "LEFT JOIN LINE ON s.line_id = LINE.id "
                + "LEFT JOIN STATION AS up_station ON s.up_station_id = up_station.id "
                + "LEFT JOIN STATION AS down_station ON s.down_station_id = down_station.id "
                + "WHERE s.line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public Optional<Section> findByLineIdAndUpStationId(Long lineId, Long upStationId) {
        try {
            String sql =
                "SELECT s.id AS section_id, LINE.id AS line_id, LINE.name AS line_name, LINE.color AS line_color, LINE.extra_fare AS extra_fare, "
                    + "up_station.id AS up_id, up_station.name AS up_name, "
                    + "down_station.id AS down_id, down_station.name AS down_name, "
                    + "distance "
                    + "FROM SECTION AS s "
                    + "LEFT JOIN LINE ON s.line_id = LINE.id "
                    + "LEFT JOIN STATION AS up_station ON s.up_station_id = up_station.id "
                    + "LEFT JOIN STATION AS down_station ON s.down_station_id = down_station.id "
                    + "WHERE s.line_id = ? AND s.up_station_id = ?";
            return Optional
                .ofNullable(
                    jdbcTemplate.queryForObject(sql, rowMapper, lineId, upStationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Section> findByLineIdAndDownStationId(Long lineId, Long downStationId) {
        try {
            String sql =
                "SELECT s.id AS section_id, LINE.id AS line_id, LINE.name AS line_name, LINE.color AS line_color, LINE.extra_fare AS extra_fare, "
                    + "up_station.id AS up_id, up_station.name AS up_name, "
                    + "down_station.id AS down_id, down_station.name AS down_name, "
                    + "distance "
                    + "FROM SECTION AS s "
                    + "LEFT JOIN LINE ON s.line_id = LINE.id "
                    + "LEFT JOIN STATION AS up_station ON s.up_station_id = up_station.id "
                    + "LEFT JOIN STATION AS down_station ON s.down_station_id = down_station.id "
                    + "WHERE s.line_id = ? AND s.down_station_id = ?";
            return Optional
                .ofNullable(
                    jdbcTemplate.queryForObject(sql, rowMapper, lineId, downStationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int deleteByLineIdAndUpStationId(Long lineId, Long upStationId) {
        String sql = "DELETE FROM SECTION WHERE line_id = ? AND up_station_id = ?";
        return jdbcTemplate.update(sql, lineId, upStationId);
    }

    public int deleteByLineIdAndDownStationId(Long lineId, Long downStationId) {
        String sql = "DELETE FROM SECTION WHERE line_id = ? AND down_station_id = ?";
        return jdbcTemplate.update(sql, lineId, downStationId);
    }

    public int delete(Section section) {
        String sql = "DELETE FROM SECTION WHERE id = ?";
        return jdbcTemplate.update(sql, section.getId());
    }

    public List<Section> findAll() {
        String sql = "SELECT s.id AS section_id, "
            + "LINE.id AS line_id, LINE.name AS line_name, LINE.color AS line_color, LINE.extra_fare AS extra_fare, "
            + "up_station.id AS up_id, "
            + "up_station.name AS up_name, "
            + "down_station.id AS down_id, "
            + "down_station.name AS down_name, "
            + "distance "
            + "FROM SECTION AS s "
            + "LEFT JOIN LINE ON s.line_id = LINE.id "
            + "LEFT JOIN STATION AS up_station ON s.up_station_id = up_station.id "
            + "LEFT JOIN STATION AS down_station ON s.down_station_id = down_station.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Line> findIncludeStationLine(Long stationId) {
        String sql = "SELECT DISTINCT LINE.id, LINE.name, LINE.color, LINE.extra_fare "
            + "FROM LINE JOIN SECTION ON LINE.id = SECTION.line_id "
            + "WHERE SECTION.up_station_id = ? OR SECTION.down_station_id = ?";

        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new Line(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("color"),
                rs.getInt("extra_fare")
            ), stationId, stationId);
    }
}