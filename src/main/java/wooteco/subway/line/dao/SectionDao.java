package wooteco.subway.line.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SectionDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> params = new HashMap();
        params.put("line_id", line.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance());
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section.getUpStation(), section.getDownStation(), section.getDistance());
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

    public void updateDistance(final long lineId, final long upStationId, final long downStationId, final Integer distance) {
        String query = "UPDATE SECTION SET distance = ? WHERE id = ? AND up_station_id = ? AND down_station_id = ?";
        jdbcTemplate.update(query, distance, lineId, upStationId, downStationId);
    }

    public List<Line> getMap() {
        String query = "SELECT SE.line_id AS line_id, SE.up_station_id AS up_station_id, " +
                "SE.down_station_id AS down_station_id, " +
                "SE.distance AS distance, US.name AS up_station_name, " +
                "DS.name AS down_station_name, L.name AS line_name, L.color AS line_color " +
                "FROM SECTION AS SE" +
                "LEFT OUTER JOIN LINE AS L ON LINE.id = SE.line_id " +
                "LEFT OUTER JOIN STATION AS US ON US.id = SE.up_station_id " +
                "LEFT OUTER JOIN STATION AS DS ON DS.id = SE.down_station_id";

        return null;
//        return jdbcTemplate.query(query, (result, rowNum) -> {
//           new Sections()
//        });
    }
}