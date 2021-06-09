package wooteco.subway.line.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.application.DuplicatedFieldException;
import wooteco.subway.exception.application.NonexistentTargetException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.line.exception.LineCompositionException;
import wooteco.subway.station.domain.Station;

@Repository
public class LineDao {

    private static final int SUCCESSFUL_EXECUTED_COUNT = 1;

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
        params.put("extraFare", line.getExtraFare());

        try {
            Long lineId = insertAction.executeAndReturnKey(params).longValue();
            return new Line(lineId, line.getName(), line.getColor(), line.getExtraFare());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedFieldException("노선이름: " + line.getName());
        }
    }

    public List<Line> findAll() {
        String sql =
            "select L.id as line_id, L.name as line_name, L.color as line_color, L.extraFare as line_extraFare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id ";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<Long, List<Map<String, Object>>> resultByLine = result.stream()
            .collect(Collectors.groupingBy(it -> (Long) it.get("line_id")));
        return resultByLine.values()
            .stream()
            .map(this::mapLine)
            .collect(Collectors.toList());
    }

    private Line mapLine(List<Map<String, Object>> result) {
        if (result.size() == 0) {
            throw new LineCompositionException("노선 데이터 생성에 필요한 데이터를 찾지 못했습니다.");
        }

        List<Section> sections = extractSections(result);

        return new Line(
            (Long) result.get(0).get("LINE_ID"),
            (String) result.get(0).get("LINE_NAME"),
            (String) result.get(0).get("LINE_COLOR"),
            (Integer) result.get(0).get("LINE_EXTRAFARE"),
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
                    new Station((Long) it.getValue().get(0).get("UP_STATION_ID"),
                        (String) it.getValue().get(0).get("UP_STATION_Name")),
                    new Station((Long) it.getValue().get(0).get("DOWN_STATION_ID"),
                        (String) it.getValue().get(0).get("DOWN_STATION_Name")),
                    (int) it.getValue().get(0).get("SECTION_DISTANCE")))
            .collect(Collectors.toList());
    }

    public Line findById(Long id) {
        String sql =
            "select L.id as line_id, L.name as line_name, L.color as line_color, L.extraFare as line_extraFare, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id " +
                "WHERE L.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, id);
        return mapLine(result);
    }

    public void update(Line line) {
        String sql = "update LINE set name = ?, color = ?, extraFare = ? where id = ?";
        try {
            int updatedCount = jdbcTemplate
                .update(sql, line.getName(), line.getColor(), line.getExtraFare(), line.getId());
            verifyExecution(updatedCount, line.getId());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedFieldException(String.format("노선명: %s", line.getName()));
        }
    }

    private void verifyExecution(int executedCount, Long id) {
        if (executedCount < SUCCESSFUL_EXECUTED_COUNT) {
            throw new NonexistentTargetException("노선ID: " + id);
        }
    }

    public void deleteById(Long id) {
        int deletedCount = jdbcTemplate.update("delete from Line where id = ?", id);
        verifyExecution(deletedCount, id);
    }
}
