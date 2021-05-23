package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.DuplicatedEmailException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.*;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        try {
            Member member = memberDao.insert(request.toMember());
            return MemberResponse.of(member);
        } catch(DuplicateKeyException e) {
            throw new DuplicatedEmailException("중복된 회원이 존재합니다.");
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public void updatePassword(LoginMember loginMember, PasswordRequest req) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        checkCurrentPassword(member.getPassword(), req.getCurrentPassword());
        memberDao.update(new Member(member.getId(), member.getEmail(), req.getNewPassword(), member.getAge()));
    }

    public AgeResponse updateAge(LoginMember loginMember, AgeRequest age) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(new Member(member.getId(), member.getEmail(), member.getPassword(), age.getAge()));
        return new AgeResponse(member.getId(), age.getAge());
    }

    private void checkCurrentPassword(String password, String currentPassword) {
        if (!currentPassword.equals(password)) {
            throw new IllegalArgumentException("입력하신 현재 비밀번호가 올바르지 않습니다.");
        }
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void checkExistEmail(String email) {
        if (memberDao.findByEmail(email) != null) {
            throw new DuplicatedEmailException("중복된 이메일이 존재합니다.");
        }
    }
}
