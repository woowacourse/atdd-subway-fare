package wooteco.subway.member.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class MemberDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Member> rowMapper = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("age")
            );


    public MemberDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("id");
    }

    public Member insert(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = :email, password = :password, age = :age where id = :id";
        Map<String, Object> param = new HashMap<>();
        param.put("email", member.getEmail());
        param.put("password", member.getPassword());
        param.put("age", member.getAge());
        param.put("id", member.getId());
        namedParameterJdbcTemplate.update(sql, param);
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = :id";
        namedParameterJdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

    public Optional<Member> findById(Long id) {
        String sql = "select * from MEMBER where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        final List<Member> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return result.stream().findAny();
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from MEMBER where email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource("email", email);
        final List<Member> result = namedParameterJdbcTemplate.query(sql, params, rowMapper);
        return result.stream().findAny();
    }
}
