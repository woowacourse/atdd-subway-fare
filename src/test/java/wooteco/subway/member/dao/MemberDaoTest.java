package wooteco.subway.member.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import wooteco.subway.exception.notfound.MemberNotFoundException;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
class MemberDaoTest {

    private MemberDao memberDao;
    private Member member1;
    private Member member2;

    public MemberDaoTest(DataSource dataSource) {
        this.memberDao = new MemberDao(dataSource);
    }

    @BeforeEach
    void setUp() {
        member1 = new Member("test@test.com", "1234", 23);
        member2 = new Member("member@test.com", "5678", 12);
    }

    @DisplayName("회원 정보 데이터베이스에 저장한다.")
    @Test
    void insert() {
        //when
        Member insertMember = memberDao.insert(member1);

        //then
        assertThat(insertMember.getEmail()).isEqualTo(member1.getEmail());
        assertThat(insertMember.getAge()).isEqualTo(member1.getAge());
        assertThat(insertMember.getPassword()).isEqualTo(member1.getPassword());
    }

    @DisplayName("Email이 null인 회원 정보를 데이터베이스에 저장한다.")
    @Test
    void insertByNullEmail() {
        //when then
        assertThatThrownBy(() -> {
            memberDao.insert(new Member((String) null, "1234", 10));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("Password가 null인 회원 정보를 데이터베이스에 저장한다.")
    @Test
    void insertByNullPassword() {
        //when then
        assertThatThrownBy(() -> {
            memberDao.insert(new Member("null@email.com", null, 10));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("Age가 null인 상태로 회원 정보를 데이터베이스에 저장한다.")
    @Test
    void insertByNullAge() {
        //when then
        assertThatThrownBy(() -> {
            memberDao.insert(new Member("null@email.com", null, 10));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("데이터베이스의 회원 정보를 수정한다.")
    @Test
    void update() {
        //given
        Member insertMember = memberDao.insert(member1);

        //when
        memberDao.update(new Member(insertMember.getId(), member2.getEmail(), member2.getPassword(), member2.getAge()));
        Member updatedMember = memberDao.findById(insertMember.getId()).orElseThrow(MemberNotFoundException::new);

        //then
        assertThat(updatedMember.getId()).isEqualTo(insertMember.getId());
        assertThat(updatedMember.getEmail()).isEqualTo(member2.getEmail());
        assertThat(updatedMember.getPassword()).isEqualTo(member2.getPassword());
        assertThat(updatedMember.getAge()).isEqualTo(member2.getAge());
    }

    @DisplayName("데이터베이스의 회원 정보를 이미 존재하는 회원 email로 수정한다.")
    @Test
    void updateByDuplicateEmail() {
        //given
        memberDao.insert(member1);
        Member insertMember = memberDao.insert(member2);

        //when then
        assertThatThrownBy(() ->
                memberDao.update(new Member(insertMember.getId(), member1.getEmail(), member2.getPassword(), member2.getAge()))
        ).isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("데이터베이스에 존재하지 않는 회원 정보를 수정한다.")
    @Test
    void updateByNotExistMember() {
        //when then
        assertThatThrownBy(() ->
                memberDao.update(member1)
        ).isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("회원 ID를 사용해서 회원 정보를 삭제한다.")
    @Test
    void deleteById() {
        //given
        Member insertMember = memberDao.insert(member1);

        //when
        memberDao.deleteById(insertMember.getId());
        Optional<Member> deletedMember = memberDao.findById(member1.getId());

        //then
        assertThat(deletedMember).isEqualTo(Optional.empty());
    }

    @DisplayName("존재하지 않는 회원 ID를 사용해서 회원 정보를 삭제한다.")
    @Test
    void deleteByNotExistentId() {
        //when then
        assertThatThrownBy(() -> memberDao.deleteById(member1.getId())).isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("회원 ID로 회원 정보를 가져온다.")
    @Test
    void findById() {
        //given
        Member insertMember = memberDao.insert(member1);

        //when
        Member findMember = memberDao.findById(insertMember.getId()).orElseThrow(MemberNotFoundException::new);

        //then
        assertThat(findMember).isEqualTo(insertMember);
    }

    @DisplayName("존재하지 않는 회원 ID로 회원 정보를 가져온다.")
    @Test
    void findByNotExistId() {
        //when
        Optional<Member> findMember = memberDao.findById(member1.getId());

        //then
        assertThat(findMember).isEqualTo(Optional.empty());
    }

    @DisplayName("회원 Email로 회원 정보를 가져온다.")
    @Test
    void findByEmail() {
        //when
        Optional<Member> findMember = memberDao.findByEmail(member1.getEmail());

        //then
        assertThat(findMember).isEqualTo(Optional.empty());
    }

    @DisplayName("존재하지 않는 회원 Email로 회원 정보를 가져온다.")
    @Test
    void findByNotExistEmail() {
        //when
        Optional<Member> findMember = memberDao.findByEmail(member1.getEmail());

        //then
        assertThat(findMember).isEqualTo(Optional.empty());
    }
}