package wooteco.subway.member.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class MemberDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate, dataSource);
    }

    @Test
    void insert() {
        Member member = memberDao.insert(new Member(1L, "joel@joel", "password", 25));
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getEmail()).isEqualTo("joel@joel");
        assertThat(member.getPassword()).isEqualTo("password");
        assertThat(member.getAge()).isEqualTo(25);
    }

    @Test
    void update() {
        Long id = 1L;
        final Member member = new Member(id, "joel@joel", "password", 25);
        memberDao.insert(member);

        memberDao.update(new Member(id, "test@test", "pw", 20));
        final Member memberById = memberDao.findById(id).get();

        assertThat(memberById.getId()).isEqualTo(id);
        assertThat(memberById.getPassword()).isEqualTo("pw");
        assertThat(memberById.getEmail()).isEqualTo("test@test");
        assertThat(memberById.getAge()).isEqualTo(20);
    }

    @Test
    void deleteById() {
        Long id = 1L;
        final Member member = new Member(id, "joel@joel", "password", 25);
        memberDao.insert(member);

        memberDao.deleteById(id);

        final Optional<Member> memberById = memberDao.findById(id);
        assertThat(memberById.isPresent()).isFalse();
    }

    @Test
    void findById() {
        Long id = 1L;
        final Member member = new Member(id, "joel@joel", "password", 25);
        memberDao.insert(member);

        final Member memberById = memberDao.findById(id).get();
        assertThat(memberById.getEmail()).isEqualTo("joel@joel");
        assertThat(memberById.getAge()).isEqualTo(25);
        assertThat(memberById.getPassword()).isEqualTo("password");
    }

    @Test
    void findByEmail() {
        Long id = 1L;
        String email = "joel@joel";
        final Member member = new Member(id, email, "password", 25);
        memberDao.insert(member);

        final Member memberById = memberDao.findByEmail(email).get();
        assertThat(memberById.getEmail()).isEqualTo(email);
        assertThat(memberById.getAge()).isEqualTo(25);
        assertThat(memberById.getPassword()).isEqualTo("password");
    }
}
