package wooteco.auth.service;

import org.springframework.stereotype.Service;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.domain.Member;
import wooteco.auth.web.dto.request.MemberRequest;
import wooteco.auth.web.dto.response.MemberResponse;
import wooteco.common.exception.badrequest.MemberNotFoundException;
import wooteco.common.exception.unauthorizationexception.UnAuthorizationException;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.getId())
                .orElseThrow(UnAuthorizationException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);
        final Member updatedMember = new Member(member.getId(), memberRequest.getEmail(),
                memberRequest.getPassword(), memberRequest.getAge());
        memberDao.update(updatedMember);
        return MemberResponse.of(updatedMember);
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);
        memberDao.deleteById(member.getId());
    }

    public boolean isExistMember(String email) {
        return memberDao.findByEmail(email)
                .isPresent();
    }
}
