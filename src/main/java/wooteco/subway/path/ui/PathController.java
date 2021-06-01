package wooteco.subway.path.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationMember;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.InvalidPathException;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {

    private final PathService pathService;
    private final FareService fareService;

    @Autowired
    public PathController(PathService pathService, FareService fareService) {
        this.pathService = pathService;
        this.fareService = fareService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationMember LoginMember loginMember,
                                                 @RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(pathService.findPath(source, target, loginMember));
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity pathException(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
