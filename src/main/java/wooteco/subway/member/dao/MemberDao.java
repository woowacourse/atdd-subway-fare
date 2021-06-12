package wooteco.subway.member.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;

@Repository
public class MemberDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    private RowMapper<Member> rowMapper = (rs, rowNum) ->
            new Member(
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

    public Member insert(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(params)
                .longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = ?, password = ?, age = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{member.getEmail(), member.getPassword(), member.getAge(), member.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Member findByEmail(String email) {
        try {
            String sql = "select * from MEMBER where email = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, email);
        } catch (Exception e) {
            throw new AuthorizationException("존재하지 않는 이메일 정보입니다.");
        }
    }

    public boolean isExistEmail(String email) {
        String sql = "SELECT EXISTS (SELECT id FROM MEMBER where email = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, email);
    }
}
