package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.auth.WrongEmailException;
import wooteco.subway.exception.duplication.EmailDuplicatedException;
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
        int counts = memberDao.countByEmail(request.getEmail());
        if (counts > 0) {
            throw new EmailDuplicatedException();
        }
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(WrongEmailException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(WrongEmailException::new);
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(WrongEmailException::new);
        memberDao.deleteById(member.getId());
    }
}
