package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.EmailNotFoundException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
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
                .orElseThrow(EmailNotFoundException::new);
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
                    .orElseThrow(EmailNotFoundException::new);
            return new LoginMember(member.getId(), member.getEmail(), member.getAge());
        } catch (Exception e) {
            return new LoginMember();
        }
    }
}
