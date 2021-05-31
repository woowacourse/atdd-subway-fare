package wooteco.subway.line.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.notfound.LineNotFoundException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.Money;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    private SqlParameterSource parameterSource(Line line) {
        return new MapSqlParameterSource()
                .addValue("id", line.getId())
                .addValue("name", line.getName())
                .addValue("color", line.getColor())
                .addValue("extraFare", line.moneyValue());
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("extra_fare", line.getExtraFare().money());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor(), line.getExtraFare());
    }

    public Optional<Line> findById(Long id) {
        try {
            String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, L.extra_fare as line_extra_fare ," +
                    "S.id as section_id, S.distance as section_distance, " +
                    "UST.id as up_station_id, UST.name as up_station_name, " +
                    "DST.id as down_station_id, DST.name as down_station_name " +
                    "from LINE L \n" +
                    "left outer join SECTION S on L.id = S.line_id " +
                    "left outer join STATION UST on S.up_station_id = UST.id " +
                    "left outer join STATION DST on S.down_station_id = DST.id " +
                    "WHERE L.id = :id";
            Map<String, Long> param = Collections.singletonMap("id", id);
            List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList(sql, param);
            return Optional.of(mapLine(result));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = :name, color = :color, extra_fare = :extraFare where id = :id";
        int count = namedParameterJdbcTemplate.update(sql, parameterSource(newLine));
        if (count < 1) {
            throw new LineNotFoundException();
        }
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
                Fare.of(new Money((Integer) result.get(0).get("LINE_EXTRA_FARE"))),
                new Sections(sections));
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
        String sql = "delete from Line where id = :id";
        Map<String, Long> param = Collections.singletonMap("id", id);
        namedParameterJdbcTemplate.update(sql, param);
    }

    public boolean existLineName(String name) {
        String sql = "select count(*) from line where name = :name limit 1";
        Map<String, String> param = Collections.singletonMap("name", name);
        int count = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class);
        return count > 0;
    }

    public boolean existLineColor(String color) {
        String sql = "select count(*) from line where color = :color limit 1";
        Map<String, String> param = Collections.singletonMap("color", color);
        int count = namedParameterJdbcTemplate.queryForObject(sql, param, Integer.class);
        return count > 0;
    }
}