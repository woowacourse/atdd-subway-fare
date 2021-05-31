package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthenticationAgePrincipal;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.ui.farepolicy.FarePolicy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationAgePrincipal FarePolicy farePolicy,
                                                 @RequestParam Long source,
                                                 @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPath(source, target, farePolicy));
    }
}
