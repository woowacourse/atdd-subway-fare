package wooteco.subway.member.application;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.conflict.AlreadyExistEmailException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberEmailCheckRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

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

        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(
            new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(),
                memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void validateDuplicateEmail(MemberEmailCheckRequest request) {
        List<Member> members = memberDao.findAllByEmail(request.getEmail());
        if (!members.isEmpty()) {
            throw new AlreadyExistEmailException();
        }
    }

    public Member findByEmail(String email) {
        return memberDao.findByEmail(email);
    }
}
