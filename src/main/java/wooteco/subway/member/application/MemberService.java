package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.User;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicateEmailException;
import wooteco.subway.member.exception.EmailUpdateTrialException;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        validatePossibleEmail(request.getEmail());
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public void checkPossibleEmail(EmailRequest request) {
        validatePossibleEmail(request.getEmail());
    }

    private void validatePossibleEmail(String email) {
        final Optional<Member> memberByEmail = memberDao.findByEmail(email);
        if (memberByEmail.isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    public MemberResponse findMember(User user) {
        Member member = memberDao.findByEmail(user.getEmail())
                .orElseThrow(AuthorizationException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(User user, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(user.getEmail())
                .orElseThrow(AuthorizationException::new);

        if (!member.isSameEmail(memberRequest.getEmail())) {
            throw new EmailUpdateTrialException();
        }

        member.update(memberRequest.getAge(), memberRequest.getPassword());
        memberDao.update(member);
    }

    public void deleteMember(User user) {
        Member member = memberDao.findByEmail(user.getEmail())
                .orElseThrow(AuthorizationException::new);
        memberDao.deleteById(member.getId());
    }
}
