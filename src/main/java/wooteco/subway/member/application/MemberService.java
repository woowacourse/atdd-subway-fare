package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.badrequest.DuplicateEmailException;
import wooteco.subway.exception.notfound.MemberNotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.*;

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
            throw new DuplicateEmailException();
        }
    }

    public MemberResponse findMember(LoginMember loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(LoginMember loginMember, MemberInfoUpdateRequest memberInfoUpdateRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);
        Member updateMember = member.update(member.getEmail(), member.getPassword(), memberInfoUpdateRequest.getAge());
        memberDao.update(updateMember);
        return MemberResponse.of(updateMember);
    }

    public void updateMemberPassword(LoginMember loginMember, MemberPasswordUpdateRequest memberPasswordUpdateRequest) {
        Member member = memberDao.findByEmail(loginMember.getEmail()).orElseThrow(MemberNotFoundException::new);
        member.validateUpdatePassword(memberPasswordUpdateRequest.getCurrentPassword(), memberPasswordUpdateRequest.getNewPassword());
        memberDao.update(new Member(member.getId(), member.getEmail(), memberPasswordUpdateRequest.getNewPassword(), member.getAge()));
    }

    public void deleteMember(LoginMember loginMember) {
        memberDao.deleteById(loginMember.getId());
    }

    public void checkDuplicateEmail(DuplicateEmailCheckRequest request) {
        if (memberDao.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
    }
}
