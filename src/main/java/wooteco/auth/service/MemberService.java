package wooteco.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.common.exception.notfound.MemberNotFoundException;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.domain.Member;
import wooteco.auth.web.dto.request.MemberRequest;
import wooteco.auth.web.dto.response.MemberResponse;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public MemberResponse updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);
        Member updatedMember = new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge());
        memberDao.update(updatedMember);
        return MemberResponse.of(updatedMember);
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);
        memberDao.deleteById(member.getId());
    }

    public Boolean existsMember(String email) {
        return memberDao.findByEmail(email).isPresent();
    }
}
