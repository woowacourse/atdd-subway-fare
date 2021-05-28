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
            Member member = memberDao.findByEmail(request.getEmail());
            member.checkPassword(request.getPassword());
            String token = jwtTokenProvider.createToken(request.getEmail());
            return new TokenResponse(token);
        } catch (Exception e) {
            throw new AuthorizationException("이메일 혹은 비밀번호를 다시 확인해주세요");
        }
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException("다시 로그인 후 시도해주세요");
        }

        String email = jwtTokenProvider.getEmail(credentials);
        try {
            Member member = memberDao.findByEmail(email);
            return new LoginMember(member.getId(), member.getEmail(), member.getAge());
        } catch (Exception e) {
            return new LoginMember();
        }
    }

    public void validateToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthorizationException("다시 로그인 후 시도해주세요");
        }
    }
}
