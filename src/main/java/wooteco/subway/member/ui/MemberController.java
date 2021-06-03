package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody @Valid MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/exists")
    public ResponseEntity<Void> checkDuplicateEmail(@RequestBody @Valid DuplicateEmailCheckRequest request) {
        memberService.checkDuplicateEmail(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody @Valid MemberInfoUpdateRequest memberInfoUpdateRequest) {
        MemberResponse memberResponse = memberService.updateMember(loginMember, memberInfoUpdateRequest);
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping("/me/pw")
    public ResponseEntity<MemberResponse> updateMemberPasswordOfMine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody @Valid MemberPasswordUpdateRequest memberPasswordUpdateRequest) {
        memberService.updateMemberPassword(loginMember, memberPasswordUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }
}
