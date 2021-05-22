package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.net.URI;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/api/members/" + member.getId())).build();
    }

    @PostMapping("/exists")
    public ResponseEntity<Void> existMember(@Email @RequestBody String email) {
        memberService.existMember(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/me/pw")
    public ResponseEntity<Void> updatePasswordMemberOfMine(@AuthenticationPrincipal LoginMember loginMember,
                                                           @Valid @RequestBody MemberPasswordRequest param) {
        memberService.updateMemberPassword(loginMember, param);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    public ResponseEntity<MemberAgeResponse> updateAgeMemberOfMine(@AuthenticationPrincipal LoginMember loginMember,
                                                                   @Valid @RequestBody MemberAgeRequest param) {
        MemberAgeResponse member = memberService.updateMemberAge(loginMember, param);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }
}
