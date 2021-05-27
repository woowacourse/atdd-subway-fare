package wooteco.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.Member;
import wooteco.auth.infrastructure.JwtTokenProvider;
import wooteco.auth.web.dto.request.TokenRequest;
import wooteco.auth.web.dto.response.TokenResponse;
import wooteco.common.exception.forbidden.AuthorizationException;

@Service
@Transactional
public class AuthService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberDao.findByEmail(request.getEmail())
                .orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(token);
    }
}
