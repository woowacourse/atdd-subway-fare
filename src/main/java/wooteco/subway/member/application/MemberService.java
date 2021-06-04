package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.InvalidEmailException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.User;
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
        Member findMember = memberDao.insert(request.toMember());
        return MemberResponse.of(findMember);
    }

    public MemberResponse findMember(User user) {
        Member findMember = memberDao.findByEmail(user.getEmail())
            .orElseThrow(InvalidEmailException::new);

        return MemberResponse.of(findMember);
    }

    public void validateDuplicateEmail(String email) {
        if (memberDao.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException();
        }
    }

    @Transactional
    public void updateMember(User user, MemberRequest memberRequest) {
        Member findMember = memberDao.findByEmail(user.getEmail())
            .orElseThrow(InvalidEmailException::new);

        memberDao.update(new Member(findMember.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    @Transactional
    public void deleteMember(User user) {
        Member findMember = memberDao.findByEmail(user.getEmail())
            .orElseThrow(InvalidEmailException::new);

        memberDao.deleteById(findMember.getId());
    }
}
