package wooteco.subway.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.exception.DuplicatedIdException;
import wooteco.subway.member.exception.MismatchIdPasswordException;

import java.sql.PreparedStatement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(MemberDao.class)
@ActiveProfiles("test")
public class MemberDaoTest {

    private static final String EMAIL = "kang@kang.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MemberDao memberDao;

    @Test
    @DisplayName("정상적인 멤버 삽입")
    public void insert() {
        //when
        Member member = memberDao.insert(new Member(EMAIL, PASSWORD, AGE));

        //then
        assertThat(member).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Member(EMAIL, PASSWORD, AGE));
    }

    @Test
    @DisplayName("수정 성공")
    public void update() {
        //given
        long id = insert(EMAIL, PASSWORD, AGE);

        //when
        memberDao.update(new Member(id, "email2@email.com", "password2", 21));

        //then
        String email = jdbcTemplate.queryForObject("select email from MEMBER where id = ?", String.class, id);
        assertThat(email).isEqualTo("email2@email.com");
    }

    @Test
    @DisplayName("삭제 성공")
    public void deleteMemberById() {
        //given
        long id = insert(EMAIL, PASSWORD, AGE);
        Boolean beforeDeletedResult = jdbcTemplate.queryForObject("select exists (select * from MEMBER where id = ?)", Boolean.class, id);
        assertThat(beforeDeletedResult).isTrue();

        //when
        memberDao.deleteById(id);

        //then
        Boolean deleteDeletedResult = jdbcTemplate.queryForObject("select exists (select * from MEMBER where id = ?)", Boolean.class, id);
        assertThat(deleteDeletedResult).isFalse();
    }

    @Test
    @DisplayName("Id 또는 Email로 조회")
    public void findByIdAndEmail() {
        //given
        long id = insert(EMAIL, PASSWORD, AGE);

        //when
        Member findedMemberById = memberDao.findById(id);
        Member findedMemberByEmail = memberDao.findByEmail(EMAIL);

        //then
        assertThat(findedMemberById).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Member(EMAIL, PASSWORD, AGE));
        assertThat(findedMemberByEmail).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Member(EMAIL, PASSWORD, AGE));
    }


    private long insert(String email, String password, int age) {
        String sql = "insert into MEMBER (email ,password, age) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setInt(3, age);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
