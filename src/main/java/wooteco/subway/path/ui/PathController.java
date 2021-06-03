package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthenticationMember;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.User;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@ModelAttribute PathRequest pathRequest, @AuthenticationPrincipal
        User user) {
        return ResponseEntity.ok(pathService.findPath(pathRequest.sourceId(),
            pathRequest.targetId(), user));
    }
}
