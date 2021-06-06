package wooteco.subway.path.ui;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.domain.User;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@Api(tags = "경로 관련 기능")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal User user,
                                                 @RequestParam Long source, @RequestParam Long target) {
        final PathResponse pathResponse = pathService.findPath(user, source, target);
        return ResponseEntity.ok(pathResponse);
    }
}
