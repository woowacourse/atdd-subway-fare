package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.RequestUser;
import wooteco.subway.member.dto.ChangeAgeRequest;
import wooteco.subway.member.dto.ChangePasswordRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicateEmailException;
import wooteco.subway.member.exception.InvalidPasswordException;
import wooteco.subway.member.exception.SamePasswordException;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberDao.exists(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(RequestUser requestUser) {
        Member member = memberDao.findByEmail(requestUser.getEmail());
        return MemberResponse.of(member);
    }

    public void changeAge(RequestUser requestUser, ChangeAgeRequest request) {
        Member member = memberDao.findByEmail(requestUser.getEmail());
        memberDao.update(new Member(member.getId(), member.getEmail(), member.getPassword(), request.getAge()));
    }

    public void changePassword(RequestUser requestUser, ChangePasswordRequest request) {
        Member member = memberDao.findByEmail(requestUser.getEmail());
        if (!member.getPassword().equals(request.getCurrentPassword())) {
            throw new InvalidPasswordException();
        }

        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new SamePasswordException();
        }

        memberDao.update(new Member(member.getId(), member.getEmail(), request.getNewPassword(), member.getAge()));
    }

    public void deleteMember(RequestUser requestUser) {
        Member member = memberDao.findByEmail(requestUser.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void checkEmailExists(String email) {
        if (memberDao.exists(email)) {
            throw new DuplicateEmailException();
        }
    }
}
