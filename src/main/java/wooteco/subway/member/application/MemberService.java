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
        validateDuplicateEmail(request.getEmail());
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    private void validateDuplicateEmail(String email) {
        if (memberDao.findByEmail(email).isPresent()) {
            throw new DuplicateException("이미 존재하는 이메일입니다.");
        }
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(LoginMember loginMember) {
        Member member = findByEmail(loginMember);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = findByEmail(loginMember);
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(),
                memberRequest.getPassword(), memberRequest.getAge()));
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        Member member = findByEmail(loginMember);
        memberDao.deleteById(member.getId());
    }

    private Member findByEmail(LoginMember loginMember) {
        return memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new AuthorizationException("존재하지 않는 이메일입니다."));
    }
}
