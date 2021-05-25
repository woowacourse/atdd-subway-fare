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
import wooteco.subway.path.application.FareService;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.Optional;

@Validated
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private PathService pathService;
    private FareService fareService;
    private AuthService authService;

    public PathController(PathService pathService, FareService fareService, AuthService authService) {
        this.pathService = pathService;
        this.fareService = fareService;
        this.authService = authService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(HttpServletRequest httpServletRequest,
                                                 @Positive(message = "올바르지 않는 역 아이디입니다.") @RequestParam Long source,
                                                 @Positive(message = "올바르지 않는 역 아이디입니다.") @RequestParam Long target) {
        String token = AuthorizationExtractor.extract(httpServletRequest);
        Optional<LoginMember> optionalMember = authService.findMemberByToken(token);

        SubwayPath path = pathService.findPath(source, target);
        int fare = fareService.calculate(path.distance(), path.extraFare(), MemberType.of(optionalMember));

        return ResponseEntity.ok(PathResponse.of(path, fare));
    }
}
