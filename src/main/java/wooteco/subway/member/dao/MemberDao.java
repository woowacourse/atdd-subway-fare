package wooteco.subway.member.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.notfound.MemberNotFoundException;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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

    private SqlParameterSource parameterSource(Member member) {
        return new BeanPropertySqlParameterSource(member);
    }

    public MemberDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Member insert(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = :email, password = :password, age = :age where id = :id";
        int count = namedParameterJdbcTemplate.update(sql, parameterSource(member));
        if (count < 1) {
            throw new MemberNotFoundException();
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = :id";
        Map<String, Long> param = Collections.singletonMap("id", id);
        int count = namedParameterJdbcTemplate.update(sql, param);
        if (count < 1) {
            throw new MemberNotFoundException();
        }
    }

    public Optional<Member> findById(Long id) {
        try {
            String sql = "select * from MEMBER where id = :id";
            Map<String, Long> param = Collections.singletonMap("id", id);
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmail(String email) {
        try {
            String sql = "select * from MEMBER where email = :email";
            Map<String, String> param = Collections.singletonMap("email", email);
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
