package wooteco.subway.auth.application;

import static wooteco.subway.exception.SubwayExceptions.*;

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
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        if (memberDao.isExistByEmail(request.getEmail())) {
            Member member = memberDao.findByEmail(request.getEmail());
            member.checkPassword(request.getPassword());
            String token = jwtTokenProvider.createToken(request.getEmail());
            return new TokenResponse(token);
        }
        throw MISMATCH_ID_PASSWORD.makeException();
    }

    public LoginMember findLoginMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        if (memberDao.isExistByEmail(email)) {
            Member member = memberDao.findByEmail(email);
            return new LoginMember(member.getId(), member.getEmail(), member.getAge());
        }
        throw new AuthorizationException();
    }
}
