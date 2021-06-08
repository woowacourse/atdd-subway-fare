package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicateEmailException;
import wooteco.subway.member.exception.InvalidMemberInformationException;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        try {
            Member member = memberDao.insert(request.toMember());
            return MemberResponse.of(member);
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException("이미 가입된 이메일입니다.");
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(InvalidMemberInformationException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(InvalidMemberInformationException::new);
        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(InvalidMemberInformationException::new);
        memberDao.deleteById(member.getId());
    }
}
