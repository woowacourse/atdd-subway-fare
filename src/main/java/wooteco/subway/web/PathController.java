package wooteco.subway.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.web.AuthenticationPrincipal;
import wooteco.subway.service.PathService;
import wooteco.subway.web.dto.response.PathResponse;

@RestController
@RequestMapping("/api/paths")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target, @AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(pathService.findPath(source, target, loginMember));
    }
}
