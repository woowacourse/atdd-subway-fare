package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.EmailExistsResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Transactional
@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberDao.isExistEmail(request.getEmail())) {
            throw new MemberException("이미 존재하는 유저 이메일입니다.");
        }

        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {

        Member member = memberDao.findByEmail(loginMember.getEmail());

        if (!member.isEmail(memberRequest.getEmail())) {
            throw new MemberException("이메일은 변경할 수 없습니다.");
        }

        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }

    public EmailExistsResponse isExistsEmail(String email) {
        return new EmailExistsResponse(memberDao.isExistEmail(email));
    }
}
