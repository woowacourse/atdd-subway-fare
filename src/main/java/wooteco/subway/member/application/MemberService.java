package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        if (memberDao.existsEmail(request.getEmail())) {
            throw new DuplicateException("이미 존재하는 이메일입니다.");
        }
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(LoginMember loginMember) {
        validatesMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    private void validatesMember(LoginMember loginMember) {
        if (!loginMember.isPresent()) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
    }

    @Transactional
    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        validatesMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(
                new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge())
        );
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        validatesMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }
}
