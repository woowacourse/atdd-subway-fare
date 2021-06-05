package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.MemberNotFoundException;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.infrastructure.dao.MemberDao;
import wooteco.subway.member.ui.dto.MemberRequest;
import wooteco.subway.member.ui.dto.MemberResponse;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        validateToAlreadyExistEmail(request);
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        validateToAlreadyExistEmailExceptMe(memberRequest, loginMember);
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        memberDao.update(
            new Member(
                member.getId(),
                memberRequest.getEmail(),
                memberRequest.getPassword(),
                memberRequest.getAge()
            )
        );
    }

    private void validateToAlreadyExistEmail(MemberRequest request) {
        if (memberDao.existsByEmail(request.getEmail())) {
            throw new DuplicateException("이미 존재하는 email 입니다.");
        }
    }

    private void validateToAlreadyExistEmailExceptMe(MemberRequest request, LoginMember loginMember) {
        if (memberDao.existsByEmailWithoutId(request.getEmail(), loginMember.getId())) {
            throw new DuplicateException("이미 존재하는 email 입니다.");
        }
    }

    @Transactional
    public void deleteMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        memberDao.deleteById(member.getId());
    }

}
