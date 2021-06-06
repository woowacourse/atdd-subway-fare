package wooteco.subway.line.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> params = createParams(line, section);
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Section(sectionId, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public void insertSections(Line line) {
        List<Section> sections = line.getSections().getSections();
        List<Map<String, Object>> batchValues = sections.stream()
                .map(section -> createParams(line, section))
                .collect(toList());

        simpleJdbcInsert.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }

    public void deleteByLineId(Long lineId) {
        jdbcTemplate.update("delete from section where line_id = ?", lineId);
    }

    private Map<String, Object> createParams(Line line, Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", line.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance());
        return params;
    }

    public void update(List<Section> sections) {
        String sql = "update section set up_station_id = ?, "
            + "down_station_id = ?, "
            + "distance = ?"
            + "where id = ?";

        List<Object[]> params = sections.stream().map(
            section -> {
                Long upStationId = section.getUpStation().getId();
                Long downStationId = section.getDownStation().getId();
                int distance = section.getDistance();
                Long id = section.getId();

                return new Object[]{upStationId, downStationId, distance, id};
            }
        ).collect(toList());

        jdbcTemplate.batchUpdate(sql, params);
    }

    public boolean existsByStationId(Long id) {
        String sql = "select exists (select * from section where up_station_id = ? or down_station_id = ?)";

        return jdbcTemplate.queryForObject(sql, boolean.class, id, id);
    }
}