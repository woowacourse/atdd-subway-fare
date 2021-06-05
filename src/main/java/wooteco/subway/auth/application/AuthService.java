package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Guest;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.User;
import wooteco.subway.member.exception.NoMemberException;

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
            Member member = memberDao.findByEmail(request.getEmail())
                    .orElseThrow(NoMemberException::new);
            member.checkPassword(request.getPassword());
        } catch (Exception e) {
            throw new AuthorizationException();
        }
        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public User findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new Guest();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        try {
            Member member = memberDao.findByEmail(email)
                    .orElseThrow(NoMemberException::new);
            return new LoginUser(member.getId(), member.getEmail(), member.getAge());
        } catch (Exception e) {
            return new Guest();
        }
    }
}
