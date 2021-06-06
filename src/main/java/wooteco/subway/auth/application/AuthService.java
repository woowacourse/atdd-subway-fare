package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

import java.util.NoSuchElementException;

@Service
@Transactional
public class AuthService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest request) {
        if (memberDao.isExistByEmail(request.getEmail())) {
            Member member = memberDao.findByEmail(request.getEmail());
            member.checkPassword(request.getPassword());
            String token = jwtTokenProvider.createToken(String.valueOf(member.getId()));
            return new TokenResponse(token);
        }
        throw new AuthorizationException("존재하지 않는 이메일입니다.");
    }

    public LoginMember findLoginMemberByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            return new LoginMember();
        }
        String id = jwtTokenProvider.getPayload(token);
        return new LoginMember(Long.valueOf(id));
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
