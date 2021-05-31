package wooteco.subway.path.ui;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthorizationMemberPrincipal;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    @ApiOperation(value = "경로 조회", notes = "경로를 조회한다.")
    public ResponseEntity<PathResponse> findPath(@AuthorizationMemberPrincipal LoginMember loginMember,
                                                 @RequestParam Long source, @RequestParam Long target) {

        return ResponseEntity.ok(pathService.findPath(loginMember, source, target));
    }
}
