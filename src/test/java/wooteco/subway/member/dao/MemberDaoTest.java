package wooteco.subway.member.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import wooteco.subway.member.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
public
class MemberDaoTest {

    MemberDao memberDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    static Member 새로운_멤버 = new Member("email1@email.com", "password", 1234);
    static Member 동일한_이메일을_가진_멤버 = new Member("email@email.com", "password", 1234);
    static Member 삭제용_멤버 = new Member( "delete@email.com", "password", 1234);

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        memberDao.insert(new Member("email@email.com", "password", 1234));
    }

    @Test
    @DisplayName("유효한 멤버 데이터를 삽입한다.")
    void insert() {
        final Member member = memberDao.insert(새로운_멤버);
        assertThat(member.getEmail()).isEqualTo("email1@email.com");
        assertThat(member.getPassword()).isEqualTo("password");
        assertThat(member.getAge()).isEqualTo(1234);
    }

    @Test
    @DisplayName("이미 존재하는 이메일을 가진 멤버 데이터를 삽입한다.")
    void insertWithExistedEmail() {
        assertThatThrownBy(() -> memberDao.insert(동일한_이메일을_가진_멤버))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("멤버의 정보를 수정한다.")
    void update() {
        final Member member = memberDao.insert(새로운_멤버);
        final Long id = member.getId();
        final Member 아이디가_동일한_수정용_멤버 = new Member(id, "email3@email.com", "password", 1234);
        memberDao.update(아이디가_동일한_수정용_멤버);
        final Member updatedMember = memberDao.findById(id);
        assertThat(updatedMember.getEmail()).isEqualTo("email3@email.com");
        assertThat(updatedMember.getPassword()).isEqualTo("password");
        assertThat(updatedMember.getAge()).isEqualTo(1234);
    }

    @Test
    @DisplayName("Id로 멤버를 삭제한다.")
    void deleteById() {
        final Member deleteMember = memberDao.insert(삭제용_멤버);
        final Long deleteMemberId = deleteMember.getId();
        memberDao.deleteById(deleteMemberId);
        assertThatThrownBy(() -> memberDao.findById(deleteMemberId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("Id로 멤버를 찾는다.")
    void findById() {
        final Member member = memberDao.insert(새로운_멤버);
        final Member findMember = memberDao.findById(member.getId());
        assertThat(findMember).isNotNull();
        assertThat(findMember.getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("존재하지 않는 Id로 멤버를 찾는다.")
    void findByInvalidId() {
        assertThatThrownBy(() -> memberDao.findById(100L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("Email로 멤버를 찾는다.")
    void findByEmail() {
        final Member member = memberDao.findByEmail("email@email.com");
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo("email@email.com");
    }

    @Test
    @DisplayName("존재하지 않는 Email로 멤버를 찾는다.")
    void findByInvalidEmail() {
        assertThatThrownBy(() -> memberDao.findByEmail("joanne@Im.joanne"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("이메일로 멤버를 찾은 뒤 존재하면 True를 리턴한다.")
    void isExistByEmail() {
        assertThat(memberDao.isExistByEmail("email@email.com")).isTrue();
    }

    @Test
    @DisplayName("이메일로 멤버를 찾은 뒤 존재하지 않으면 False를 리턴한다.")
    void isExistByInvalidEmail() {
        assertThat(memberDao.isExistByEmail("email12312@email.com")).isFalse();
    }
}