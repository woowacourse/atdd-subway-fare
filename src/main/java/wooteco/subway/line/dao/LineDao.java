package wooteco.subway.line.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Line> rowMapper = (rs, rowNum) -> {
        Long foundId = rs.getLong("id");
        String name = rs.getString("name");
        String color = rs.getString("color");
        int extraFare = rs.getInt("extra_fare");

        return new Line(foundId, name, color, extraFare);
    };

    public LineDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
            .withTableName("LINE")
            .usingGeneratedKeyColumns("id");
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("extra_fare", line.getExtraFare());

        long key = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Line(key, line.getName(), line.getColor(), line.getExtraFare());
    }

    public Line find(Long id) {
        String sql = "SELECT * FROM LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Line> findAll() {
        String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int update(Long id, Line line) {
        String sql = "UPDATE LINE SET name = ?, color = ?, extra_fare = ? WHERE id = ?";
        return jdbcTemplate.update(sql, line.getName(), line.getColor(), line.getExtraFare(), id);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM LINE WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}