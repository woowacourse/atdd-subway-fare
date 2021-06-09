package wooteco.subway.member.application;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.EmailExistResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Transactional
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

    @Transactional(readOnly = true)
    public MemberResponse findMember(AuthMember authMember) {
        Member member = memberDao.findByEmail(authMember.getEmail());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public void updateMember(AuthMember authMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(authMember.getEmail());
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(AuthMember authMember) {
        Member member = memberDao.findByEmail(authMember.getEmail());
        memberDao.deleteById(member.getId());
    }

    public EmailExistResponse isExistEmail(String email) {
        return new EmailExistResponse(memberDao.isExistEmail(email));
    }
}
