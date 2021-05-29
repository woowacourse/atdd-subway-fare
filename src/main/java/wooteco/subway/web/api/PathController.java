package wooteco.subway.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.web.AuthenticationPrincipal;
import wooteco.subway.service.PathService;
import wooteco.subway.web.dto.response.PathResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source,
        @RequestParam Long target, @AuthenticationPrincipal
        LoginMember loginMember) {
        return ResponseEntity.ok(pathService.findPath(source, target, loginMember));
    }
}
