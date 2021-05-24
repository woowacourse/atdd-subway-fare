package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.exception.MemberNotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

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

    public MemberResponse findMember(LoginMember loginMember) throws NotFoundException {
        Member member = memberDao.findByEmail(loginMember.getEmail())
            .orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
            .orElseThrow(MemberNotFoundException::new);
        Member updatedMember = new Member(member.getId(), memberRequest.getEmail(),
            memberRequest.getPassword(), memberRequest.getAge());
        memberDao.update(updatedMember);
        return MemberResponse.of(updatedMember);
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
            .orElseThrow(MemberNotFoundException::new);
        memberDao.deleteById(member.getId());
    }

    public boolean isExistMember(String email) {
        return memberDao.findByEmail(email).isPresent();
    }
}
