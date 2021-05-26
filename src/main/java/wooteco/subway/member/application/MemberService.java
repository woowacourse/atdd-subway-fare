package wooteco.subway.member.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicatedEmailAddressException;
import wooteco.subway.member.exception.MemberNotFoundException;

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
        Member member = memberDao.findByEmail(loginMember.getEmail())
            .orElseThrow(() -> new MemberNotFoundException(loginMember.getEmail()));
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
            .orElseThrow(() -> new MemberNotFoundException(loginMember.getEmail()));
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
            .orElseThrow(() -> new MemberNotFoundException(loginMember.getEmail()));
        memberDao.deleteById(member.getId());
    }

    public void validateUniqueEmail(String email) {
        if (isDuplicatedMember(email)) {
            throw new DuplicatedEmailAddressException(email);
        }
    }

    private boolean isDuplicatedMember(String email) {
        return memberDao.findByEmail(email)
            .isPresent();
    }
}
