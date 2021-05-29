package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberDao.existsEmail(request.getEmail())) {
            throw new DuplicateException("이미 존재하는 이메일입니다.");
        }
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = findByEmail(loginMember);
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = findByEmail(loginMember);
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(),
                memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = findByEmail(loginMember);
        memberDao.deleteById(member.getId());
    }

    private Member findByEmail(LoginMember loginMember) {
        if (!memberDao.existsEmail(loginMember.getEmail())) {
            throw new AuthorizationException("존재하지 않는 이메일입니다.");
        }
        return memberDao.findByEmail(loginMember.getEmail());
    }
}
