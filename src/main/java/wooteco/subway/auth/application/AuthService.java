package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.infrastructure.ErrorCode;
import wooteco.subway.infrastructure.exception.domain.auth.AuthorizationException;
import wooteco.subway.infrastructure.exception.domain.auth.TokenNotValidException;
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
        Member member = memberDao.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.AUTH_NOT_VALID_LOGIN_INFO));

        member.checkPassword(request.getPassword());
        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new LoginMember();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        try {
            Member member = memberDao.findByEmail(email)
                    .orElseThrow(() -> new TokenNotValidException(ErrorCode.AUTH_NOT_VALID_TOKEN));

            return new LoginMember(member.getId(), member.getEmail(), member.getAge());
        } catch (Exception e) {
            return new LoginMember();
        }
    }
}
