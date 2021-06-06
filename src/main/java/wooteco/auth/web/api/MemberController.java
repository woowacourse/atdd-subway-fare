package wooteco.auth.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.auth.domain.AuthenticationPrincipal;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.service.MemberService;
import wooteco.auth.web.dto.request.MemberRequest;
import wooteco.auth.web.dto.response.MemberResponse;

import javax.validation.Valid;
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

    @GetMapping
    public ResponseEntity<Boolean> duplicateEmail(@RequestParam(name = "email") String email) {
        return ResponseEntity.ok(memberService.isExistMember(email));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal LoginMember loginMember, @Valid @RequestBody MemberRequest param) {
        final MemberResponse memberResponse = memberService.updateMember(loginMember, param);
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }
}
