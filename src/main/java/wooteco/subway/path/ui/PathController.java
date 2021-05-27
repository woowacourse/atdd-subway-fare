package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.member.domain.MemberType;
import wooteco.subway.member.domain.MemberTypeProducer;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@Validated
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@MemberTypeProducer MemberType memberType,
                                                 @RequestParam Long source,
                                                 @RequestParam Long target) {
        PathResponse body = pathService.findPath(source, target, memberType);
        return ResponseEntity.ok(body);
    }
}
