package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

@Service
@Transactional
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberDao.findByEmail(request.getEmail())
            .orElseThrow(() -> new AuthorizationException("입력한 정보가 틀립니다."));

        validateCorrectPassword(member, request);
        return new TokenResponse(
            jwtTokenProvider.createToken(request.getEmail())
        );
    }

    private void validateCorrectPassword(Member member, TokenRequest request) {
        if (!member.checkPassword(request.getPassword())) {
            throw new AuthorizationException("입력한 정보가 틀립니다.");
        }
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return LoginMember.obtainGuest();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        return memberDao.findByEmail(email)
            .map(LoginMember::new)
            .orElse(LoginMember.obtainGuest());
    }
}
