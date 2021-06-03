package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.auth.AuthorizationException;
import wooteco.subway.exception.auth.AuthorizationExceptionStatus;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.Member;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberService.findMemberByEmail(request.getEmail());
        member.checkPassword(request.getPassword());
        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginUser findMemberByToken(String credentials) {
        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberService.findMemberByEmail(email);
        return new LoginUser(member.getId(), member.getEmail(), member.getAge());
    }

    public void validate(String credentials) {
        boolean isValidToken = jwtTokenProvider.validateToken(credentials);
        if (!isValidToken) {
            throw new AuthorizationException(AuthorizationExceptionStatus.INVALID_TOKEN);
        }
    }
}
