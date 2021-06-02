package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.RequestUser;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/api/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal RequestUser loginMember, @RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPath(loginMember, source, target));
    }
}
