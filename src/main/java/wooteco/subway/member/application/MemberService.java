package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.infrastructure.dao.MemberDao;
import wooteco.subway.member.ui.dto.MemberRequest;
import wooteco.subway.member.ui.dto.MemberResponse;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }

}
