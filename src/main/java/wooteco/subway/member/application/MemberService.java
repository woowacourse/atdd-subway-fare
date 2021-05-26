package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.exception.NotFoundException;
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
            throw new DuplicatedException("이미 가입된 이메일입니다");
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        try {
            Member member = memberDao.findByEmail(loginMember.getEmail());
            return MemberResponse.of(member);
        } catch(NotFoundException e) {
            throw new NotFoundException("존재하지 않는 회원입니다");
        }
    }

    public void updatePassword(LoginMember loginMember, PasswordRequest req) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        checkCurrentPassword(member.getPassword(), req.getCurrentPassword());
        checkNewPassword(req.getCurrentPassword(), req.getNewPassword());
        memberDao.update(new Member(member.getId(), member.getEmail(), req.getNewPassword(), member.getAge()));
    }

    public AgeResponse updateAge(LoginMember loginMember, AgeRequest age) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(new Member(member.getId(), member.getEmail(), member.getPassword(), age.getAge()));
        return new AgeResponse(member.getId(), age.getAge());
    }

    private void checkNewPassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException("현재 사용 중인 비밀번호입니다. 다른 비밀번호를 입력해주세요");
        }
    }

    private void checkCurrentPassword(String password, String currentPassword) {
        if (!currentPassword.equals(password)) {
            throw new IllegalArgumentException("현재 비밀번호를 다시 확인해주세요");
        }
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void checkExistEmail(EmailCheckRequest email) {
        if (memberDao.checkExistsMemberBy(email)) {
            throw new DuplicatedException("중복된 이메일이 존재합니다");
        }
    }
}
