package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.notfound.MemberNotFoundExceptionException;
import wooteco.subway.exception.unauthorized.AuthorizationException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;

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
        try {
            Member member = memberDao.findByEmail(request.getEmail()).orElseThrow(MemberNotFoundExceptionException::new);
            member.checkPassword(request.getPassword());
            String token = jwtTokenProvider.createToken(String.valueOf(member.getId()));
            return new TokenResponse(token);
        } catch (Exception e) {
            throw new AuthorizationException();
        }
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new LoginMember();
        }

        Long id = Long.parseLong(jwtTokenProvider.getPayload(credentials));
        try {
            Member member = memberDao.findById(id).orElseThrow(MemberNotFoundExceptionException::new);
            return new LoginMember(member.getId(), member.getEmail(), member.getAge());
        } catch (Exception e) {
            return new LoginMember();
        }
    }

    public void validateToken(String accessToken) {
        if (jwtTokenProvider.validateToken(accessToken)) {
            return;
        }
        throw new AuthorizationException();
    }
}
