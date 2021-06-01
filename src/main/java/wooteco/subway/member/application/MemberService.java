package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
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

    public MemberResponse findMember(LoginMember loginMember) {
        if (!loginMember.isLogin()) {
            throw new AuthorizationException("로그인하지 않은 회원입니다.");
        }
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        if (!memberDao.isExistByEmail(loginMember.getEmail())) {
            throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
        }
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(
                new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        if (!memberDao.isExistByEmail(loginMember.getEmail())) {
            throw new MemberNotFoundException(loginMember.getEmail());
        }
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void confirmEmailIsValid(EmailCheckRequest emailCheckRequest) {
        if (memberDao.isExistByEmail(emailCheckRequest.getEmail())) {
            throw new DuplicatedException(emailCheckRequest.getEmail());
        }
    }
}
