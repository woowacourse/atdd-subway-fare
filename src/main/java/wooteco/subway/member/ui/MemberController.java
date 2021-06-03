package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        URI location = URI.create("/members/" + member.getId());
        return ResponseEntity.created(location)
                .build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginUser loginUser) {
        MemberResponse member = memberService.findMember(loginUser);
        return ResponseEntity.ok()
                .body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<Void> updateMemberOfMine(@AuthenticationPrincipal LoginUser loginUser, @RequestBody @Valid MemberRequest param) {
        memberService.updateMember(loginUser, param);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal LoginUser loginUser) {
        memberService.deleteMember(loginUser);
        return ResponseEntity.noContent()
                .build();
    }
}
