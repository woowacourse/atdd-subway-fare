package wooteco.subway.line.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        String query = "INSERT INTO LINE (name, color) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = getPreparedStatementCreator(line, query);
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return new Line(keyHolder.getKey().longValue(), line.getName(), line.getColor());
    }

    private PreparedStatementCreator getPreparedStatementCreator(Line line, String query) {
        return (connection) -> {
            PreparedStatement prepareStatement = connection.prepareStatement(query, new String[]{"id"});
            prepareStatement.setString(1, line.getName());
            prepareStatement.setString(2, line.getColor());
            return prepareStatement;
        };
    }

    public Line findById(Long id) {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as line_extra_fare,  " +
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

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getId()});
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
        Map<Long, List<Map<String, Object>>> resultByLine = result.stream().collect(Collectors.groupingBy(it -> (Long) it.get("line_id")));
        return resultByLine.entrySet().stream()
                .map(it -> mapLine(it.getValue()))
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
                (Integer) result.get(0).get("LINE_EXTRA_FARE"),
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
                                new Station((Long) it.getValue().get(0).get("UP_STATION_ID"), (String) it.getValue().get(0).get("UP_STATION_Name")),
                                new Station((Long) it.getValue().get(0).get("DOWN_STATION_ID"), (String) it.getValue().get(0).get("DOWN_STATION_Name")),
                                (int) it.getValue().get(0).get("SECTION_DISTANCE")))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from LINE where id = ?", id);
    }

    public int countByName(String name) {
        String sql = "select count(*) from LINE where name = ?";
        return jdbcTemplate.queryForObject(sql, int.class, name);
    }

    public int countByColor(String color) {
        String sql = "select count(*) from LINE where color = ?";
        return jdbcTemplate.queryForObject(sql, int.class, color);
    }

    public int countById(Long id) {
        String sql = "select count(*) from LINE where id = ?";
        return jdbcTemplate.queryForObject(sql, int.class, id);
    }
}
