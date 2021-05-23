package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.badrequest.DuplicateEmailException;
import wooteco.subway.exception.notfound.MemberNotFoundExceptionException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberInfoUpdateRequest;
import wooteco.subway.member.dto.MemberPasswordUpdateRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

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
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException();
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundExceptionException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(LoginMember loginMember, MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundExceptionException::new);
        Member updateMember = member.update(member.getEmail(), member.getPassword(), memberInfoUpdateRequest.getAge());
        memberDao.update(updateMember);
        return MemberResponse.of(updateMember);
    }

    public void updateMemberPassword(LoginMember loginMember, MemberPasswordUpdateRequest memberPasswordUpdateRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundExceptionException::new);
        member.checkPassword(memberPasswordUpdateRequest.getCurrentPassword());
        memberDao.update(new Member(member.getId(), member.getEmail(), memberPasswordUpdateRequest.getNewPassword(), member.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundExceptionException::new);
        memberDao.deleteById(member.getId());
    }

    public void checkDuplicateEmail(String email) {
        if (memberDao.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException();
        }
    }
}
