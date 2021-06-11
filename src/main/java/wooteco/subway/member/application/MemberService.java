package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.member.DuplicateEmailException;
import wooteco.subway.exception.member.InvalidPasswordException;
import wooteco.subway.exception.member.SamePasswordException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.*;

@Service
@Transactional
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        checkEmailDuplication(request.getEmail());
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    private void checkEmailDuplication(String email) {
        if (memberDao.exists(email)) {
            throw new DuplicateEmailException();
        }
    }

    public void checkEmailDuplication(MemberEmailRequest request) {
        if (memberDao.exists(request.getEmail())) {
            throw new DuplicateEmailException();
        }
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMemberPassword(LoginMember loginMember, MemberPasswordRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail());

        if (isInvalidPassword(member, memberRequest)) {
            throw new InvalidPasswordException();
        }
        if (isSamePassword(member, memberRequest)) {
            throw new SamePasswordException();
        }

        memberDao.update(new Member(member.getId(), member.getEmail(), memberRequest.getNewPassword(), member.getAge()));
    }

    private boolean isSamePassword(Member member, MemberPasswordRequest memberRequest) {
        return member.getPassword().equals(memberRequest.getNewPassword());
    }

    private boolean isInvalidPassword(Member member, MemberPasswordRequest memberRequest) {
        return !member.getPassword().equals(memberRequest.getCurrentPassword());
    }

    public MemberAgeResponse updateMemberAge(LoginMember loginMember, MemberAgeRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(new Member(member.getId(), member.getEmail(), member.getPassword(), memberRequest.getAge()));
        return MemberAgeResponse.of(memberDao.findByEmail(loginMember.getEmail()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }
}
