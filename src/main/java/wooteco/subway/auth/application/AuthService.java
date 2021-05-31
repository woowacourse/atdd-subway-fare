package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.User;

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
        try {
            Member member = memberDao.findByEmail(request.getEmail());
            member.checkPassword(request.getPassword());
        } catch (Exception e) {
            throw new AuthorizationException("이메일 또는 비밀번호를 잘못 입력하셨습니다.");
        }
        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public User findMemberByToken(String credentials) {
        validateToken(credentials);
        String email = jwtTokenProvider.getPayload(credentials);
        try {
            Member member = memberDao.findByEmail(email);
            return new LoginUser(member.getId(), member.getEmail(), member.getAge());
        } catch (Exception e) {
            throw new AuthorizationException("잘못된 토큰입니다.");
        }
    }

    public void validateToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException("토큰이 유효하지 않습니다.");
        }
    }
}
