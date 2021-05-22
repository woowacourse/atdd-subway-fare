package wooteco.subway.member.application;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.ChangeAgeRequest;
import wooteco.subway.member.dto.ChangePasswordRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicateEmailException;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (!memberDao.exists(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public void changeAge(LoginMember loginMember, ChangeAgeRequest request) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(new Member(member.getId(), member.getEmail(), member.getPassword(), request.getAge()));
    }

    public void changePassword(LoginMember loginMember, ChangePasswordRequest request) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        member.checkPassword(request.getCurrentPassword());
        memberDao.update(new Member(member.getId(), member.getEmail(), request.getNewPassword(), member.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void checkEmailExists(String email) {
        if (!memberDao.exists(email)) {
            throw new DuplicateEmailException();
        }
    }
}
