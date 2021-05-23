package wooteco.subway.member.ui;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
    public ResponseEntity createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me")
    public ResponseEntity<AgeResponse> updateAgeOfMine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody AgeRequest age) {
        AgeResponse member = memberService.updateAge(loginMember, age);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me/pw")
    public ResponseEntity updatePasswordOfMine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody PasswordRequest req) {
        memberService.updatePassword(loginMember, req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/exists")
    public ResponseEntity checkExistsMember(@RequestBody @Email String email) {
        memberService.checkExistEmail(email);
        return ResponseEntity.noContent().build();
    }
}
