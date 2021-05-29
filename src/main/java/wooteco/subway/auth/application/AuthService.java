package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.ui.dto.TokenRequest;
import wooteco.subway.auth.ui.dto.TokenResponse;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.infrastructure.dao.MemberDao;

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
        validateRightPassword(request);

        String token = jwtTokenProvider.createToken(request.getEmail());

        return new TokenResponse(token);
    }

    private void validateRightPassword(TokenRequest request) {
        validateThatEmailExists(request.getEmail());

        Member member = memberDao.findByEmail(request.getEmail());
        member.checkPassword(request.getPassword());
    }

    public LoginMember findMemberByToken(String credentials) {
        jwtTokenProvider.validateToken(credentials);
        String email = jwtTokenProvider.getPayload(credentials);

        validateThatEmailExists(email);
        Member member = memberDao.findByEmail(email);

        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    private void validateThatEmailExists(String email) {
        if (!memberDao.existsByEmail(email)) {
            throw new AuthorizationException();
        }
    }

}
