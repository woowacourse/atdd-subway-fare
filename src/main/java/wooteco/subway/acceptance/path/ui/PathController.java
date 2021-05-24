package wooteco.subway.acceptance.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.acceptance.auth.domain.AuthenticationPrincipal;
import wooteco.subway.acceptance.member.domain.LoginMember;
import wooteco.subway.acceptance.path.application.PathService;
import wooteco.subway.acceptance.path.dto.PathResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal LoginMember loginMember,
                                                 @RequestParam Long source, @RequestParam Long target) {
        LoginMember convertedLoginMember = unLoginMemberWrapper(loginMember);
        PathResponse pathResponse = pathService.findPath(convertedLoginMember, source, target);
        return ResponseEntity.ok(pathResponse);
    }

    private LoginMember unLoginMemberWrapper(@AuthenticationPrincipal LoginMember loginMember) {
        if (loginMember.getId() == null) {
            return new LoginMember(null, null, -1);
        }
        return loginMember;
    }
}
