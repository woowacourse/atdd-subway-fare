package wooteco.subway.member.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Member> rowMapper = (rs, rowNum) ->
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
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public Optional<Member> existEmail(String email) {
        String sql = "select * from MEMBER where exists(select * from MEMBER where email = ?) limit 1";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updatePassword(Member member) {
        String sql = "update MEMBER set password = ? where id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getAge(), member.getId());
    }

    public void updateAge(Member member) {
        String sql = "update MEMBER set email = ?, password = ?, age = ? where id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getAge(), member.getId());
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Member findById(Long id) {
        String sql = "select * from MEMBER where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Member findByEmail(String email) {
        String sql = "select * from MEMBER where email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    public Member findByEmailAndPassword(String email, String password) {
        String sql = "select * from MEMBER where email = ?, password = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email, password);
    }
}
