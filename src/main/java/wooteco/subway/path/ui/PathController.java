package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationMember;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.InvalidPathException;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationMember LoginMember loginMember,
                                                 @RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPath(loginMember, source, target));
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity pathException(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
