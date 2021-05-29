package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;

import javax.validation.Valid;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@ModelAttribute @Valid PathRequest pathRequest,
                                                 @AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(pathService.findPath(pathRequest.getDeparture(), pathRequest.getArrival(), loginMember));
    }
}
