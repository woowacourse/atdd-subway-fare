package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.EmailExistsResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Transactional
@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        checkDuplicatedEmail(request.getEmail());

        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(LoginUser loginUser) {
        Member member = memberDao.findByEmail(loginUser.getEmail());
        return MemberResponse.of(member);
    }

    public void updateMember(LoginUser loginUser, MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(loginUser.getEmail());

        String requestEmail = memberRequest.getEmail();
        if (!requestEmail.equals(loginUser.getEmail())) {
            checkDuplicatedEmail(requestEmail);
        }

        memberDao.update(new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    private void checkDuplicatedEmail(String requestEmail) {
        if (memberDao.isExistEmail(requestEmail)) {
            throw new MemberException("이미 존재하는 유저 이메일입니다.");
        }
    }

    public void deleteMember(LoginUser loginUser) {
        Member member = memberDao.findByEmail(loginUser.getEmail());
        memberDao.deleteById(member.getId());
    }

    public EmailExistsResponse isExistsEmail(String email) {
        return new EmailExistsResponse(memberDao.isExistEmail(email));
    }
}
