package wooteco.subway.member.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.User;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicatedEmailAddressException;

@Service
public class MemberService {

    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        checkEmail(request.getEmail());
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(User user) {
        LoginMember loginMember = translateLoginMember(user);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMember(User user, MemberRequest memberRequest) {
        LoginMember loginMember = translateLoginMember(user);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(
            new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(),
                memberRequest.getAge()));
    }

    public void deleteMember(User user) {
        LoginMember loginMember = translateLoginMember(user);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void checkEmail(String email) {
        try {
            memberDao.findByEmail(email);
        } catch (DataAccessException e) {
            return;
        }
        throw new DuplicatedEmailAddressException(email);
    }

    public LoginMember translateLoginMember(User user) {
        if (user.isGuest()) {
            throw new AuthorizationException("해당하는 유저가 존재하지 않습니다.");
        }

        return (LoginMember) user;
    }
}
