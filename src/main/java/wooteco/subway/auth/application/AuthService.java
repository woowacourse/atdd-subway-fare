package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.domain.MemberType;

import java.util.Optional;

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
        Member member = memberDao.findByEmail(request.getEmail());
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public MemberType findMemberTypeByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return MemberType.NONE;
        }

        String email = jwtTokenProvider.getPayload(credentials);
        try {
            Member member = memberDao.findByEmail(email);
            return MemberType.of(member);
        } catch (Exception e) {
            return MemberType.NONE;
        }
    }

    public Optional<LoginMember> findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return Optional.empty();
        }

        String email = jwtTokenProvider.getPayload(credentials);
        try {
            Member member = memberDao.findByEmail(email);
            return Optional.of(new LoginMember(member.getId(), member.getEmail(), member.getAge()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
