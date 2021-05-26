package wooteco.subway.member.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicatedIdException;

@Service
public class MemberService {

    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        try {
            memberDao.findByEmail(request.getEmail());
        } catch (EmptyResultDataAccessException exception) {
            Member member = memberDao.insert(request.toMember());
            return MemberResponse.of(member);
        }
        throw new DuplicatedIdException("중복된 ID 입니다.");
    }

    public MemberResponse findMember(String email) {
        Member member = memberDao.findByEmail(email);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        return findMember(loginMember.getEmail());
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.update(
            new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(),
                memberRequest.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }
}
