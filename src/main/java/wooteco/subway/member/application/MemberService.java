package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicateEmailException;
import wooteco.subway.member.exception.EmailNotFoundRuntimeException;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberDao.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = findMemberByEmail(loginMember);
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = findMemberByEmail(loginMember);
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = findMemberByEmail(loginMember);
        memberDao.deleteById(member.getId());
    }

    private Member findMemberByEmail(LoginMember loginMember) {
        return memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(EmailNotFoundRuntimeException::new);
    }
}
