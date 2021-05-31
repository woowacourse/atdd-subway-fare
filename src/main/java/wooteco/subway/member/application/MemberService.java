package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.exception.AuthException;
import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.MemberException;

import java.util.Objects;

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
            throw new SubwayCustomException(MemberException.DUPLICATED_EMAIL_EXCEPTION);
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        validateMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new SubwayCustomException(MemberException.NOT_FOUND_MEMBER_EXCEPTION));
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        validateMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new SubwayCustomException(MemberException.NOT_FOUND_MEMBER_EXCEPTION));

        try {
            memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
        } catch (DuplicateKeyException e) {
            throw new SubwayCustomException(MemberException.DUPLICATED_EMAIL_EXCEPTION);
        }
    }

    public void deleteMember(LoginMember loginMember) {
        validateMember(loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new SubwayCustomException(MemberException.NOT_FOUND_MEMBER_EXCEPTION));
        memberDao.deleteById(member.getId());
    }

    private void validateMember(LoginMember member) {
        if(Objects.isNull(member.getId())) {
            throw new SubwayCustomException(AuthException.NOT_EXIST_EMAIL_EXCEPTION);
        }
    }
}
