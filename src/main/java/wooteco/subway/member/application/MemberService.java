package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.auth.WrongEmailException;
import wooteco.subway.exception.duplication.EmailDuplicatedException;
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
        validateDuplicatedEmail(request.getEmail());
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    private void validateDuplicatedEmail(String email) {
        if (memberDao.findByEmail(email).isPresent()) {
            throw new EmailDuplicatedException();
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(WrongEmailException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(WrongEmailException::new);
        Member newMember = new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge());
        memberDao.update(newMember);
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(WrongEmailException::new);
        memberDao.deleteById(member.getId());
    }

    public Member findMemberByEmail(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(WrongEmailException::new);
    }
}
