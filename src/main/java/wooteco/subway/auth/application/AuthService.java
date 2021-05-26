package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.AuthorizationMember;
import wooteco.subway.member.domain.GuestMember;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        try {
            AuthorizationMember authorizationMember = memberDao.findByEmail(request.getEmail());
            authorizationMember.checkPassword(request.getPassword());
        } catch (Exception e) {
            throw new AuthorizationException();
        }
        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public Member findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new GuestMember();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        try {
            AuthorizationMember authorizationMember = memberDao.findByEmail(email);
            return new LoginMember(authorizationMember.getId(), authorizationMember.getEmail(), authorizationMember.getAge());
        } catch (Exception e) {
            return new GuestMember();
        }
    }

    public void validateToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException("jwt 토큰이 유효하지 않습니다.");
        }
    }
}
