package wooteco.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.common.exception.forbidden.AuthorizationException;
import wooteco.auth.web.dto.TokenRequest;
import wooteco.auth.web.dto.TokenResponse;
import wooteco.auth.infrastructure.JwtTokenProvider;
import wooteco.common.exception.notfound.MemberNotFoundException;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.domain.Member;

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
                .orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(token);
    }

//    public LoginMember findMemberByToken(String credentials) {
//        if (!jwtTokenProvider.validateToken(credentials)) {
//            return new LoginMember();
//        }
//
//        String email = jwtTokenProvider.getPayload(credentials);
//        try {
//            Member member = memberDao.findByEmail(email)
//                    .orElseThrow(MemberNotFoundException::new);
//            return new LoginMember(member.getId(), member.getEmail(), member.getAge());
//        } catch (Exception e) {
//            return new LoginMember();
//        }
//    }
}
