package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.User;
import wooteco.subway.member.dto.EmailCheckRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.MemberModifyNotAllowedException;
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

    @Transactional
    public void updateMember(User user, MemberRequest memberRequest) {
        if (memberDao.isNotExistByEmail(user.getEmail())) {
            throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
        }
        final Member member = memberDao.findByEmail(user.getEmail());
        if (member.checkDifferentUserByEmail(memberRequest.getEmail())) {
            throw new MemberModifyNotAllowedException("이메일은 수정할 수 없습니다.");
        }
        final Member updateMember = member.update(memberRequest.getEmail(),
                memberRequest.getPassword(), memberRequest.getAge());
        memberDao.update(updateMember);
    }

    public void deleteMember(User user) {
        if (memberDao.isNotExistByEmail(user.getEmail())) {
            throw new MemberNotFoundException(user.getEmail());
        }
        memberDao.deleteByEmail(user.getEmail());
    }

    public void confirmEmailIsValid(EmailCheckRequest emailCheckRequest) {
        if (memberDao.isExistByEmail(emailCheckRequest.getEmail())) {
            throw new DuplicatedException(emailCheckRequest.getEmail());
        }
    }
}
