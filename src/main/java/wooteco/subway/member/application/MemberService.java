package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.util.Objects;

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
        Long id = loginMember.getId();
        if (Objects.isNull(id)) {
            throw new AuthorizationException();
        }
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findById(loginMember.getId());
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findById(loginMember.getId());
        memberDao.deleteById(member.getId());
    }

    public void checkEmail(EmailRequest request) {
        if (memberDao.existsByEmail(request.getEmail())) {
            throw new DuplicateException("중복되는 이메일입니다.");
        }
    }
}
