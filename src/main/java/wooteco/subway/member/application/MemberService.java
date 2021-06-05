package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.DuplicatedException;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.User;
import wooteco.subway.member.dto.*;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        try {
            Member member = memberDao.insert(request.toMember());
            return MemberResponse.of(member);
        } catch(DuplicateKeyException e) {
            throw new DuplicatedException("이미 가입된 이메일입니다");
        }
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(User loginMember) {
        Member member = findMemberByEmail(loginMember);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updatePassword(User loginMember, PasswordRequest req) {
        Member member = findMemberByEmail(loginMember);
        member.validatePassword(req.getCurrentPassword(), req.getNewPassword());
        memberDao.update(new Member(member.getId(), member.getEmail(), req.getNewPassword(), member.getAge()));
    }

    @Transactional
    public AgeResponse updateAge(User loginMember, AgeRequest req) {
        Member member = findMemberByEmail(loginMember);
        memberDao.update(new Member(member.getId(), member.getEmail(), member.getPassword(), req.getAge()));
        return new AgeResponse(member.getId(), req.getAge());
    }

    @Transactional
    public void deleteMember(User loginMember) {
        Member member = findMemberByEmail(loginMember);
        memberDao.deleteById(member.getId());
    }

    @Transactional(readOnly = true)
    public void checkExistEmail(EmailCheckRequest email) {
        if (memberDao.checkExistsMemberBy(email)) {
            throw new DuplicatedException("중복된 이메일이 존재합니다");
        }
    }

    private void checkExistsMember(User member) {
        if (member == null) {
            throw new NotFoundException("존재하지 않는 회원입니다");
        }
    }

    private Member findMemberByEmail(User loginMember) {
        Member member = memberDao.findByEmail(loginMember.getEmail());
        checkExistsMember(member);
        return member;
    }
}
