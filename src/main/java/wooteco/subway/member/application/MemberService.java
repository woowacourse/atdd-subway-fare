package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.member.DuplicateEmailException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.*;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
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
        Member member = memberDao.findByEmailAndPassword(loginMember.getEmail(), memberRequest.getCurrentPassword());
        memberDao.update(new Member(member.getId(), member.getEmail(), memberRequest.getNewPassword(), member.getAge()));
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
