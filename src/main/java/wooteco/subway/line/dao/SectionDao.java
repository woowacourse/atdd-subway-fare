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
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

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
        params.put("distance", section.getDistanceASInt());
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
                    params.put("distance", section.getDistanceASInt());
                    return params;
                })
                .collect(Collectors.toList());

        simpleJdbcInsert.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }

    public void updateDistance(final long lineId, final long upStationId, final long downStationId, final Integer distance) {
        String query = "UPDATE SECTION SET distance = ? WHERE id = ? AND up_station_id = ? AND down_station_id = ?";
        jdbcTemplate.update(query, distance, lineId, upStationId, downStationId);
    }

    public boolean doesSectionNotExists(final long lineId, final long upStationId, final long downStationId) {
        String query = "SELECT NOT EXISTS (SELECT * FROM SECTION WHERE (id = ? AND up_station_id = ? AND down_station_id = ?))";
        return jdbcTemplate.queryForObject(query, Boolean.class, lineId, upStationId, downStationId);
    }
}