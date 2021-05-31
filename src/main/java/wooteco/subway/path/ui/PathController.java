package wooteco.subway.path.ui;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private final AuthService authService;
    private final PathService pathService;

    public PathController(AuthService authService, PathService pathService) {
        this.authService = authService;
        this.pathService = pathService;
    }

    @GetMapping("/api/paths")
    public ResponseEntity<PathResponse> findPath(HttpServletRequest request, @RequestParam Long source,
        @RequestParam Long target) {
        LoginMember loginMember = authService.findMemberByToken(AuthorizationExtractor.extract(request));
        return ResponseEntity.ok().body(pathService.findPath(source, target, loginMember));
    }
}
