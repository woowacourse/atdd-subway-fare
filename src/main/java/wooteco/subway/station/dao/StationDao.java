package wooteco.subway.station.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.application.DuplicatedFieldException;
import wooteco.subway.exception.application.NonexistentTargetException;
import wooteco.subway.exception.application.ReferenceConstraintException;
import wooteco.subway.station.domain.Station;

@Repository
public class StationDao {

    private static final int SUCCESSFUL_EXECUTED_COUNT = 1;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
        new Station(
            rs.getLong("id"),
            rs.getString("name")
        );

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        try {
            Long id = insertAction.executeAndReturnKey(params).longValue();
            return new Station(id, station.getName());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedFieldException("역이름: " + station.getName());
        }
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Station> findById(Long id) {
        String sql = "select * from STATION where id = ?";

        return Optional.ofNullable(
            DataAccessUtils.singleResult(jdbcTemplate.query(sql, rowMapper, id))
        );
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        try {
            int deletedCount = jdbcTemplate.update(sql, id);
            verifyExecution(deletedCount, id);
        } catch (DataIntegrityViolationException e) {
            throw new ReferenceConstraintException(
                String.format("해당역을 참조하는 부분이 있어 삭제할 수 없습니다. (역ID: %s)", id)
            );
        }
    }

    private void verifyExecution(int executedCount, Long id) {
        if (executedCount < SUCCESSFUL_EXECUTED_COUNT) {
            throw new NonexistentTargetException("역ID: " + id);
        }
    }
}
