package wooteco.subway.line.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LineDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("LINE")
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

    public Optional<Line> findById(Long id) {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as line_extra_fare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id " +
                "WHERE L.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{id});
        return mapLine(result);
    }

    public Optional<Line> findByName(String name) {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as line_extra_fare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id " +
                "WHERE L.name = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{name});
        return mapLine(result);
    }

    public Optional<Line> findByColor(String color) {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as line_extra_fare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id " +
                "WHERE L.color = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{color});
        return mapLine(result);
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ?, extra_fare = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getExtraFare(), newLine.getId()});
    }

    public List<Line> findAll() {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as line_extra_fare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id ";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return mapLines(result);
    }

    public List<Line> findIncludingStation(Long stationId) {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as line_extra_fare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id " +
                "where L.name IN (select distinct LINE.NAME " +
                "from LINE join SECTION on LINE.id = SECTION.line_id " +
                "where SECTION.up_station_id = ? or SECTION.down_station_id = ?)";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{stationId}, new Object[]{stationId});
        return mapLines(result);
    }

    private List<Line> mapLines(List<Map<String, Object>> result) {
        Map<Long, List<Map<String, Object>>> resultByLine = result.stream().collect(Collectors.groupingBy(it -> (Long) it.get("line_id")));
        return resultByLine.values().stream()
                .map(this::mapLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<Line> mapLine(List<Map<String, Object>> result) {
        if (result.size() == 0) {
            return Optional.empty();
        }

        List<Section> sections = extractSections(result);

        return Optional.of(new Line(
                (Long) result.get(0).get("LINE_ID"),
                (String) result.get(0).get("LINE_NAME"),
                (String) result.get(0).get("LINE_COLOR"),
                (int) result.get(0).get("LINE_EXTRA_FARE"),
                new Sections(sections)));
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
                                new Station((Long) it.getValue().get(0).get("UP_STATION_ID"), (String) it.getValue().get(0).get("UP_STATION_Name")),
                                new Station((Long) it.getValue().get(0).get("DOWN_STATION_ID"), (String) it.getValue().get(0).get("DOWN_STATION_Name")),
                                (int) it.getValue().get(0).get("SECTION_DISTANCE")))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from LINE where id = ?", id);
    }
}