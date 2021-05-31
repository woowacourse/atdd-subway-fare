package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.infrastructure.exception.duplicate.EmailDuplicatedException;
import wooteco.subway.infrastructure.exception.not_found.MemberNotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        checkDuplicatedEmail(request.getEmail());
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberDao.findByEmail(email);
        if (member.isPresent()) {
            throw new EmailDuplicatedException();
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);
        memberDao.deleteById(member.getId());
    }
}
