package wooteco.subway.line.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SectionDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> params = extractSectionArgs(line, section);
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    private Map<String, Object> extractSectionArgs(Line line, Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", line.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance());
        return params;
    }

    public void deleteByLineId(Long lineId) {
        String sql = "delete from SECTION where line_id = :lineId";
        Map<String, Long> param = Collections.singletonMap("lineId", lineId);
        namedParameterJdbcTemplate.update(sql, param);
    }

    public void insertSections(Line line) {
        List<Section> sections = line.getSections().getSections();
        List<Map<String, Object>> batchValues = sections.stream()
                .map(section -> extractSectionArgs(line, section))
                .collect(Collectors.toList());
        simpleJdbcInsert.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }

    public boolean existStation(Long stationId) {
        String sql = "select count(*) from section where up_station_id = :upStationId OR down_station_id = :downStationId limit 1";
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", stationId);
        params.put("downStationId", stationId);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count > 0;
    }
}