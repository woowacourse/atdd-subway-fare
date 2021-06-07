package wooteco.subway.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@MockitoSettings
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberDao memberDao;

    @DisplayName("멤버 생성 성공한다.")
    @Test
    void createMemberSuccess() {
        // given
        String email = "pkeugine@gmail.com";
        String password = "password";
        Member member = new Member(1L, email, password, 20);
        MemberRequest memberRequest = new MemberRequest(email, password, 20);
        when(memberDao.exists(memberRequest.getEmail())).thenReturn(false);
        when(memberDao.insert(any())).thenReturn(member);

        // when
        MemberResponse memberResponse = memberService.createMember(memberRequest);

        // then
        assertAll(
                () -> assertThat(memberResponse).extracting("id").isEqualTo(1L),
                () -> assertThat(memberResponse).extracting("email").isEqualTo(email),
                () -> assertThat(memberResponse).extracting("age").isEqualTo(20)
        );
    }

    @DisplayName("멤버 생성 실패한다. - 이미 존재하는 이메일")
    @Test
    void createMemberFail_existingEmail() {
        // given
        String email = "pkeugine@gmail.com";
        String password = "password";
        MemberRequest memberRequest = new MemberRequest(email, password, 20);
        when(memberDao.exists(memberRequest.getEmail())).thenReturn(true);

        // when // then
        assertThatThrownBy(() -> memberService.createMember(memberRequest))
                .isInstanceOf(DuplicatedMemberException.class);
    }

    @DisplayName("멤버 정보 조회 성공")
    @Test
    void findMemberSuccess() {
        // given
        String email = "pkeugine@gmail.com";
        String password = "password";
        LoginMember loginMember = new LoginMember(1L, email, 20);
        Member member = new Member(1L, email, password, 20);
        MemberRequest memberRequest = new MemberRequest(email, password, 20);

        when(memberDao.findByEmail(loginMember.getEmail())).thenReturn(member);

        // when
        MemberResponse memberResponse = memberService.findMember(loginMember);

        // then
        assertAll(
                () -> assertThat(memberResponse).extracting("id").isEqualTo(1L),
                () -> assertThat(memberResponse).extracting("email").isEqualTo(email),
                () -> assertThat(memberResponse).extracting("age").isEqualTo(20)
        );
    }

    @DisplayName("멤버 정보 수정 성공")
    @Test
    void updateMemberSuccess() {
        // given
        String email = "pkeugine@gmail.com";
        String password = "password";
        LoginMember loginMember = new LoginMember(1L, email, 20);
        Member member = new Member(1L, email, password, 20);
        MemberRequest memberRequest = new MemberRequest(email, password, 20);

        when(memberDao.findByEmail(memberRequest.getEmail())).thenReturn(member);
        // when
        memberService.updateMember(loginMember, memberRequest);

        // then
        verify(memberDao).update(any());
    }

    @DisplayName("멤버 정보 삭제 성공")
    @Test
    void deleteMemberSuccess() {
        // given
        String email = "pkeugine@gmail.com";
        String password = "password";
        LoginMember loginMember = new LoginMember(1L, email, 20);
        Member member = new Member(1L, email, password, 20);

        when(memberDao.findByEmail(loginMember.getEmail())).thenReturn(member);
        // when
        memberService.deleteMember(loginMember);

        // then
        verify(memberDao).deleteById(member.getId());
    }

}