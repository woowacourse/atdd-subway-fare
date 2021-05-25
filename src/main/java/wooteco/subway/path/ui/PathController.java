package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.MemberType;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.Optional;

@Validated
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private PathService pathService;
    private AuthService authService;

    public PathController(PathService pathService, AuthService authService) {
        this.pathService = pathService;
        this.authService = authService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(HttpServletRequest httpServletRequest,
                                                 @Positive(message = "올바르지 않는 역 아이디입니다.") @RequestParam Long source,
                                                 @Positive(message = "올바르지 않는 역 아이디입니다.") @RequestParam Long target) {
        String token = AuthorizationExtractor.extract(httpServletRequest);
        Optional<LoginMember> optionalMember = authService.findMemberByToken(token);

        PathResponse body = pathService.findPath(source, target, MemberType.of(optionalMember));
        return ResponseEntity.ok(body);
    }
}
