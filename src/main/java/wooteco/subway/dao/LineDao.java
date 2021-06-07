package wooteco.subway.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

@Repository
public class LineDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("LINE")
            .usingGeneratedKeyColumns("id");
    }

    private RowMapper<Line> lineRowMapper() {
        return (rs, rowNum) -> {
            Long foundId = rs.getLong("id");
            final String color = rs.getString("color");
            final String name = rs.getString("name");
            return new Line(foundId, name, color);
        };
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("extra_fare", line.getExtraFare());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor());
    }

    public Line findById(Long id) {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, " +
            "S.id as section_id, S.distance as section_distance, " +
            "UST.id as up_station_id, UST.name as up_station_name, " +
            "DST.id as down_station_id, DST.name as down_station_name, " +
            "L.extra_fare as line_extra_fare " +
            "from LINE L \n" +
            "left outer join SECTION S on L.id = S.line_id " +
            "left outer join STATION UST on S.up_station_id = UST.id " +
            "left outer join STATION DST on S.down_station_id = DST.id " +
            "WHERE L.id = :id";

        final List<Map<String, Object>> result = jdbcTemplate
            .queryForList(sql, new MapSqlParameterSource("id", id));

        return mapLine(result);
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = :name, color = :color where id = :id";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(newLine));
    }

    public List<Line> findAll() {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, " +
            "S.id as section_id, S.distance as section_distance, " +
            "UST.id as up_station_id, UST.name as up_station_name, " +
            "DST.id as down_station_id, DST.name as down_station_name, " +
            "L.extra_fare as line_extra_fare " +
            "from LINE L \n" +
            "left outer join SECTION S on L.id = S.line_id " +
            "left outer join STATION UST on S.up_station_id = UST.id " +
            "left outer join STATION DST on S.down_station_id = DST.id ";

        final List<Map<String, Object>> result = jdbcTemplate
            .queryForList(sql, new MapSqlParameterSource());
        Map<Long, List<Map<String, Object>>> resultByLine = result.stream()
            .collect(Collectors.groupingBy(it -> (Long) it.get("line_id")));
        return resultByLine.values().stream()
            .map(this::mapLine)
            .collect(Collectors.toList());
    }

    private Line mapLine(List<Map<String, Object>> result) {
        if (result.size() == 0) {
            throw new RuntimeException();
        }

        List<Section> sections = extractSections(result);

        return new Line(
            (Long) result.get(0).get("LINE_ID"),
            (String) result.get(0).get("LINE_NAME"),
            (String) result.get(0).get("LINE_COLOR"),
            (int) result.get(0).get("LINE_EXTRA_FARE"),
            new Sections(sections));
    }

    private List<Section> extractSections(List<Map<String, Object>> result) {
        if (result.isEmpty() || result.get(0).get("SECTION_ID") == null) {
            return Collections.EMPTY_LIST;
        }
        return result.stream()
            .collect(Collectors.groupingBy(it -> it.get("SECTION_ID")))
            .entrySet()
            .stream()
            .map(it ->
                new Section(
                    (Long) it.getKey(),
                    new Station((Long) it.getValue().get(0).get("UP_STATION_ID"),
                        (String) it.getValue().get(0).get("UP_STATION_Name")),
                    new Station((Long) it.getValue().get(0).get("DOWN_STATION_ID"),
                        (String) it.getValue().get(0).get("DOWN_STATION_Name")),
                    (int) it.getValue().get(0).get("SECTION_DISTANCE")))
            .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        final String sql = "delete from Line where id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    public Optional<Line> findByName(String name) {
        String sql = "select * from LINE where name = :name";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("name", name), lineRowMapper()).stream().findAny();
    }

    public Optional<Line> findByColor(String color) {
        String sql = "select * from LINE where color = :color";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("color", color), lineRowMapper()).stream().findAny();
    }
}