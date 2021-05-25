package wooteco.subway.line.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("SECTION")
            .usingGeneratedKeyColumns("id");
    }

    public List<Section> findByLineId(Long lineId) {
        String sql = "SELECT se.id, se.line_id, se.up_station_id, \n"
            + "st1.name as \"up_station_name\", \n"
            + "se.down_station_id, \n"
            + "st2.name as \"down_station_name\", \n"
            + "se.distance\n"
            + "FROM SECTION se\n"
            + "LEFT OUTER JOIN STATION st1\n"
            + "ON se.up_station_id = st1.id\n"
            + "LEFT OUTER JOIN STATION st2\n"
            + "ON se.down_station_id = st2.id\n"
            + "WHERE se.line_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Section(
                rs.getLong("id"),
                new Station(rs.getLong("up_station_id"), rs.getString("up_station_name")),
                new Station(rs.getLong("down_station_id"), rs.getString("down_station_name")),
                rs.getInt("distance")
            ), lineId);
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> params = new HashMap();
        params.put("line_id", line.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance());
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section.getUpStation(), section.getDownStation(),
            section.getDistance());
    }

    public void deleteByLineId(Long lineId) {
        jdbcTemplate.update("delete from SECTION where line_id = ?", lineId);
    }

    public void insertSections(Line line) {
        List<Section> sections = line.getSections().getSections();
        List<Map<String, Object>> batchValues = sections.stream()
            .map(section -> {
                Map<String, Object> params = new HashMap<>();
                params.put("line_id", line.getId());
                params.put("up_station_id", section.getUpStation().getId());
                params.put("down_station_id", section.getDownStation().getId());
                params.put("distance", section.getDistance());
                return params;
            })
            .collect(Collectors.toList());

        simpleJdbcInsert.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }
}