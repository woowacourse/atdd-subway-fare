package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        final String email = request.getEmail();
        if (memberDao.existsByEmail(email)) {
            throw new DuplicatedException(String.format("이미 존재하는 이메일입니다. (입력한 값: %s)", email));
        }
        final Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    private void validateDuplicatedMemberEmail(final String email) {
        if (!memberDao.existsByEmail(email)) {
            throw new NotFoundException(String.format("해당하는 유저를 찾을 수 없습니다. (입력한 값: %s)", email));
        }
    }

    public void checkDuplicatedMemberEmail(final EmailRequest emailRequest) {
        final String email = emailRequest.getEmail();
        if (memberDao.existsByEmail(email)) {
            throw new DuplicatedException(String.format("이미 존재하는 이메일입니다. (입력한 값: %s)", email));
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        validateDuplicatedMemberEmail(loginMember.getEmail());
        final Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        validateDuplicatedMemberEmail(loginMember.getEmail());
        final Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(
            new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        validateDuplicatedMemberEmail(loginMember.getEmail());
        final Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }
}
