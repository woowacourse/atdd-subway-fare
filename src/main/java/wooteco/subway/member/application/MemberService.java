package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.auth.WrongEmailException;
import wooteco.subway.exception.badrequest.duplication.EmailDuplicatedException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        validateEmailDuplication(request.getEmail());
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    private void validateEmailDuplication(String email) {
        memberDao.findByEmail(email)
                .ifPresent(member -> {
                    throw new EmailDuplicatedException();
                });
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = findMemberByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public Member findMemberByEmail(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(WrongEmailException::new);
    }

    @Transactional
    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = findMemberByEmail(loginMember.getEmail());
        String currentEmail = member.getEmail();
        String newEmail = memberRequest.getEmail();
        if (!currentEmail.equals(newEmail)) {
            validateEmailDuplication(newEmail);
        }
        Member newMember = new Member(member.getId(), newEmail, memberRequest.getPassword(), memberRequest.getAge());
        memberDao.update(newMember);
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        Member member = findMemberByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }
}
