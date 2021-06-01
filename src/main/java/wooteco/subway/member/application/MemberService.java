package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.AuthorizationException;
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
        validateDuplicatedEmail(request);
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    private void validateDuplicatedEmail(MemberRequest request) {
        if (memberDao.existsEmail(request.getEmail())) {
            throw new DuplicateException("이미 존재하는 이메일입니다.");
        }
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(LoginMember loginMember) {
        Member member = findByEmail(loginMember);
        return MemberResponse.of(member);
    }

    private Member findByEmail(LoginMember loginMember) {
        validatesMember(loginMember);
        return memberDao.findByEmail(loginMember.getEmail());
    }

    private void validatesMember(LoginMember loginMember) {
        if (loginMember.isAnonymous()) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
    }

    @Transactional
    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = findByEmail(loginMember);
        memberDao.update(
                new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge())
        );
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        Member member = findByEmail(loginMember);
        memberDao.deleteById(member.getId());
    }
}
