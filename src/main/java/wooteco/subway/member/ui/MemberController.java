package wooteco.subway.member.ui;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.domain.User;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import javax.validation.Valid;
import java.net.URI;

@Api(tags = "회원 관련 기능")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @PostMapping("/members/email-check")
    public ResponseEntity<Void> checkPossibleEmail(@RequestBody EmailRequest request) {
        memberService.checkPossibleEmail(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal User user) {
        validateLoginMember(user);
        MemberResponse member = memberService.findMember(user);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal User user, @RequestBody MemberRequest param) {
        validateLoginMember(user);
        memberService.updateMember(user, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal User user) {
        validateLoginMember(user);
        memberService.deleteMember(user);
        return ResponseEntity.noContent().build();
    }

    private void validateLoginMember(User user) {
        if (user.isLoggedIn()) {
            return;
        }
        throw new AuthorizationException();
    }
}
