package wooteco.subway.member.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.exception.DuplicatedIdException;
import wooteco.subway.member.exception.MismatchIdPasswordException;

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
        try {
            Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
            return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedIdException();
        }

    }

    public void update(Member member) {
        String sql = "update MEMBER set email = ?, password = ?, age = ? where id = ?";
        if (jdbcTemplate.update(sql, new Object[]{member.getEmail(), member.getPassword(), member.getAge(), member.getId()}) == 0) {
            throw new MismatchIdPasswordException();
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = ?";
        if (jdbcTemplate.update(sql, id) == 0) {
            throw new MismatchIdPasswordException();
        }
    }

    public Member findById(Long id) {
        String sql = "select * from MEMBER where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new MismatchIdPasswordException();
        }
    }

    public Member findByEmail(String email) {
        String sql = "select * from MEMBER where email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, email);
        } catch (EmptyResultDataAccessException e) {
            throw new MismatchIdPasswordException();
        }
    }

    public boolean checkEmailDuplicated(String email) {
        String sql = "select exists (select * from MEMBER where email = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, email);
    }
}
