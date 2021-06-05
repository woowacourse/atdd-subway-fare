package wooteco.subway.path.ui;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthPrincipal;
import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    @ApiOperation(value = "역과 역 사이 경로 조회")
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") Long source, @RequestParam("target") Long target,
                                                 @AuthPrincipal AuthMember authMember) {
        PathResponse path = pathService.findPath(source, target, authMember);
        return ResponseEntity.ok(path);
    }
}
