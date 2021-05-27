package wooteco.subway.member.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.member.domain.Age;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Member> rowMapper = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    new Age(rs.getInt("age"))
            );

    public MemberDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("id");
    }

    public Member insert(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());
        params.put("age", member.getAgeAsInt());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = ?, password = ?, age = ? where id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getAgeAsInt(), member.getId());
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

    public boolean doesEmailExist(final String email) {
        String query = "SELECT EXISTS (SELECT * FROM MEMBER WHERE email = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, email);
    }
}
