package wooteco.subway.member.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate, dataSource);
        jdbcTemplate.execute("create table if not exists MEMBER ( id bigint auto_increment not null, email varchar(255) not null unique, password varchar(255) not null, age int not null, primary key(id) )");
    }

    @DisplayName("주어진 이메일에 해당하는 개수를 확인한다.")
    @Test
    void countsByEmail() {
        memberDao.insert(new Member("test@naver.com", "pass123", 17));

        int counts = memberDao.countByEmail("test@naver.com");

        assertThat(counts).isEqualTo(1);
    }
}
