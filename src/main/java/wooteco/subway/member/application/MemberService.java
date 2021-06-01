package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.User;
import wooteco.subway.member.dto.EmailCheckRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.MemberNotFoundException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberDao.isExistByEmail(request.getEmail())) {
            throw new DuplicatedException(request.getEmail());
        }
        final Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(User user) {
        if (user.isGuest()) {
            throw new AuthorizationException("로그인하지 않은 회원입니다.");
        }
        Member member = memberDao.findByEmail(user.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMember(User user, MemberRequest memberRequest) {
        if (!memberDao.isExistByEmail(user.getEmail())) {
            throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
        }
        Member member = memberDao.findByEmail(user.getEmail());
        memberDao.update(
                new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(User user) {
        if (!memberDao.isExistByEmail(user.getEmail())) {
            throw new MemberNotFoundException(user.getEmail());
        }
        Member member = memberDao.findByEmail(user.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void confirmEmailIsValid(EmailCheckRequest emailCheckRequest) {
        if (memberDao.isExistByEmail(emailCheckRequest.getEmail())) {
            throw new DuplicatedException(emailCheckRequest.getEmail());
        }
    }
}
