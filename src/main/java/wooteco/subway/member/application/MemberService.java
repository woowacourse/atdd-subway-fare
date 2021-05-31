package wooteco.subway.member.application;

import java.util.Objects;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.exception.SubwayAuthException;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.SubwayMemberException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        try {
            Member member = memberDao.insert(request.toMember());
            return MemberResponse.of(member);
        } catch (DuplicateKeyException exception) {
            throw new SubwayCustomException(SubwayMemberException.DUPLICATE_EMAIL_EXCEPTION);
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        validateMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return MemberResponse.of(member);
    }

    private void validateMember(LoginMember loginMember) {
        if (Objects.isNull(loginMember.getId())) {
            throw new SubwayCustomException(SubwayAuthException.NOT_EXIST_MEMBER_EXCEPTION);
        }
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        validateMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        try {
            memberDao.update(
                new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(),
                    memberRequest.getAge()));
        } catch (DuplicateKeyException exception) {
            throw new SubwayCustomException(SubwayMemberException.DUPLICATE_EMAIL_EXCEPTION);
        }
    }

    public void deleteMember(LoginMember loginMember) {
        validateMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail());
        memberDao.deleteById(member.getId());
    }
}
