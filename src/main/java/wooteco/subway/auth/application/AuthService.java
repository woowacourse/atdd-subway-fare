package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.TokenInvalidException;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
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
        if (!memberDao.isExistByEmail(request.getEmail())) {
            throw new AuthorizationException("사용자를 찾을 수 없습니다.");
        }
        Member member = memberDao.findByEmail(request.getEmail());
        member.checkPassword(request.getPassword());
        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public Member findMemberByToken(String credentials) {
        String email = jwtTokenProvider.getPayload(credentials);
        if (!memberDao.isExistByEmail(email)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
        return memberDao.findByEmail(email);
    }

    public void validateToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new TokenInvalidException("토큰이 유효하지 않습니다.");
        }
    }
}
