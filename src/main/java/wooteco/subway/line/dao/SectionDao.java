package wooteco.subway.line.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SectionDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
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

        return new Section.Builder(sectionId, section.getDistance())
                .upStation(section.getUpStation())
                .downStation(section.getDownStation())
                .build();
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

    public boolean isExistByLineId(Long lineId) {
        String sql = "select EXISTS (select * from SECTION where line_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, lineId);
    }

    public boolean isIncludedInLine(Long id) {
        String sql = "select EXISTS (select * from SECTION where up_station_id = ? or down_station_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id, id);
    }

    public List<Long> getLineIdIfStationIncluded(Station station) {
        Long stationId = station.getId();
        String sql = "select line_id from SECTION where up_station_id = ? or down_station_id = ?";
        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> resultSet.getLong("line_id"),
                stationId,
                stationId
        );
    }
}