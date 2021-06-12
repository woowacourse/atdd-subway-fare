package wooteco.subway.auth.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.NotLoginMember;

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
        try {
            Member member = memberDao.findByEmail(request.getEmail());
            member.checkPassword(request.getPassword());
        } catch (EmptyResultDataAccessException | AuthorizationException e) {
            throw new AuthorizationException("아이디 혹은 패스워드가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public AuthMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new NotLoginMember();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        try {
            Member member = memberDao.findByEmail(email);
            return new LoginMember(member.getId(), member.getEmail(), member.getAge());
        } catch (Exception e) {
            return new NotLoginMember();
        }
    }
}
