package wooteco.subway.member.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.net.URI;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(
        @AuthenticationPrincipal LoginMember loginMember,
        @RequestBody @Valid MemberRequest memberRequest
    ) {
        MemberResponse member = memberService.updateMember(loginMember, memberRequest);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Boolean> isDuplicatedEmail(@RequestParam String email) {
        return ResponseEntity.ok(memberService.isExistMember(email));
    }
}
