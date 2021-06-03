package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.MemberRelatedException;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginUser loginUser) {
        Member member = memberDao.findByEmail(loginUser.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMember(LoginUser loginUser, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginUser.getEmail());
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginUser loginUser) {
        Member member = memberDao.findByEmail(loginUser.getEmail());
        memberDao.deleteById(member.getId());
    }

    public void checkEmail(String email) {
        if (memberDao.isDuplicateEmail(email)) {
            throw new MemberRelatedException("이미 사용된 이메일입니다.");
        }
    }

}
