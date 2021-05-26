package wooteco.subway.member.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.member.domain.AuthorizationMember;

import javax.sql.DataSource;

@Repository
public class MemberDao {
    private static int NO_ELEMENT_COUNT = 0;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<AuthorizationMember> rowMapper = (rs, rowNum) ->
        new AuthorizationMember(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("age")
        );


    public MemberDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("MEMBER")
            .usingGeneratedKeyColumns("id");
    }

    public boolean existEmail(String email) {
        String sql = "select count(*) from member where email = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count > NO_ELEMENT_COUNT;
    }

    public AuthorizationMember insert(AuthorizationMember authorizationMember) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(authorizationMember);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new AuthorizationMember(id, authorizationMember.getEmail(), authorizationMember.getPassword(), authorizationMember.getAge());
    }

    public void update(AuthorizationMember authorizationMember) {
        String sql = "update MEMBER set email = ?, password = ?, age = ? where id = ?";
        jdbcTemplate.update(sql, authorizationMember.getEmail(), authorizationMember.getPassword(), authorizationMember.getAge(), authorizationMember.getId());
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public AuthorizationMember findById(Long id) {
        String sql = "select * from MEMBER where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public AuthorizationMember findByEmail(String email) {
        String sql = "select * from MEMBER where email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }
}
