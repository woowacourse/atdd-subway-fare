package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.User;
import wooteco.subway.member.dto.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/members")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody @Valid MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @PostMapping("/exists")
    public ResponseEntity checkExistsMember(@RequestBody @Valid EmailCheckRequest emailReq) {
        memberService.checkExistEmail(emailReq);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal User loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me")
    public ResponseEntity<AgeResponse> updateAgeOfMine(@AuthenticationPrincipal User loginMember, @RequestBody @Valid AgeRequest age) {
        AgeResponse member = memberService.updateAge(loginMember, age);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me/pw")
    public ResponseEntity updatePasswordOfMine(@AuthenticationPrincipal User loginMember, @RequestBody @Valid PasswordRequest req) {
        memberService.updatePassword(loginMember, req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity deleteMemberOfMine(@AuthenticationPrincipal User loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }
}
