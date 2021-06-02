package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.notfound.NoMemberException;
import wooteco.subway.exception.unprocessableentity.DuplicateEmailException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(NoMemberException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(NoMemberException::new);
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(NoMemberException::new);
        memberDao.deleteById(member.getId());
    }

    public void checkValidation(String email) {
        memberDao.findByEmail(email)
                .ifPresent(member -> {
                    throw new DuplicateEmailException();
                });
    }
}
