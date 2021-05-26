package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.auth.InvalidTokenException;
import wooteco.subway.exception.auth.WrongEmailException;
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
                .orElseThrow(WrongEmailException::new);
        member.checkPassword(request.getPassword());
        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberDao.findByEmail(email)
                .orElseThrow(WrongEmailException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    public void validate(String credenitlas) {
        boolean isValid = jwtTokenProvider.validateToken(credenitlas);
        if (!isValid) {
            throw new InvalidTokenException();
        }
    }
}
