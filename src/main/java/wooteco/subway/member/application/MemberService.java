package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.AuthorizationMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        if (memberDao.existEmail(request.getEmail())) {
            throw new DuplicateMemberEmailException();
        }
        AuthorizationMember authorizationMember = memberDao.insert(request.toMember());
        return MemberResponse.of(authorizationMember);
    }

    public MemberResponse findMember(Member member) {
        AuthorizationMember authorizationMember = memberDao.findByEmail(member.getEmail());
        return MemberResponse.of(authorizationMember);
    }

    @Transactional
    public void updateMember(Member member, MemberRequest memberRequest) {
        AuthorizationMember authorizationMember = memberDao.findByEmail(member.getEmail());
        memberDao.update(new AuthorizationMember(authorizationMember.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    @Transactional
    public void deleteMember(Member member) {
        AuthorizationMember authorizationMember = memberDao.findByEmail(member.getEmail());
        memberDao.deleteById(authorizationMember.getId());
    }
}
