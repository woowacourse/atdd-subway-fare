package wooteco.subway.line.infrastructure.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class LineDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("extra_fare", line.getExtraFare());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor(), line.getExtraFare());
    }

    public Line findById(Long id) {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as extra_fare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from line L \n" +
                "left outer join section S on L.id = S.line_id " +
                "left outer join station UST on S.up_station_id = UST.id " +
                "left outer join station DST on S.down_station_id = DST.id " +
                "WHERE L.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapLine(result);
    }

    public void update(Line newLine) {
        String sql = "update line set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getId()});
    }

    public List<Line> findAll() {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as extra_fare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from line L \n" +
                "left outer join section S on L.id = S.line_id " +
                "left outer join station UST on S.up_station_id = UST.id " +
                "left outer join station DST on S.down_station_id = DST.id ";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<Long, List<Map<String, Object>>> resultByLine = result.stream().collect(Collectors.groupingBy(it -> (Long) it.get("line_id")));
        return resultByLine.values().stream()
                .map(this::mapLine)
                .collect(Collectors.toList());
    }

    private Line mapLine(List<Map<String, Object>> result) {
        if (result.size() == 0) {
            throw new EmptyResultDataAccessException(1);
        }

        List<Section> sections = extractSections(result);

        return new Line(
                (Long) result.get(0).get("LINE_ID"),
                (String) result.get(0).get("LINE_NAME"),
                (String) result.get(0).get("LINE_COLOR"),
                new Sections(sections),
            (Integer) result.get(0).get("EXTRA_FARE"));
    }

    private List<Section> extractSections(List<Map<String, Object>> result) {
        if (result.isEmpty() || result.get(0).get("SECTION_ID") == null) {
            return Collections.emptyList();
        }
        return result.stream()
                .collect(Collectors.groupingBy(it -> it.get("SECTION_ID")))
                .entrySet()
                .stream()
                .map(it ->
                        new Section(
                                (Long) it.getKey(),
                                new Station((Long) it.getValue().get(0).get("UP_STATION_ID"), (String) it.getValue().get(0).get("UP_STATION_Name")),
                                new Station((Long) it.getValue().get(0).get("DOWN_STATION_ID"), (String) it.getValue().get(0).get("DOWN_STATION_Name")),
                                (int) it.getValue().get(0).get("SECTION_DISTANCE")))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from line where id = ?", id);
    }

    public boolean existsById(Long id) {
        String sql = "select exists (select * from line where id = ?)";

        return jdbcTemplate.queryForObject(sql, boolean.class, id);
    }

    public boolean existsByColor(String color) {
        String sql = "select exists (select * from line where color = ?)";

        return jdbcTemplate.queryForObject(sql, boolean.class, color);
    }

    public boolean existsByColorExceptId(String color, Long id) {
        String sql = "select exists (select * from line where color = ? and id <> ?)";

        return jdbcTemplate.queryForObject(sql, boolean.class, color, id);
    }
}