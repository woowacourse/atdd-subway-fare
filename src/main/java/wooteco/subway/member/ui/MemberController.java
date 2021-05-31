package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal Optional<LoginMember> loginMember) {
        final LoginMember memberLoggedIn = validateLoginMember(loginMember);
        MemberResponse member = memberService.findMember(memberLoggedIn);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal Optional<LoginMember> loginMember, @RequestBody MemberRequest param) {
        final LoginMember memberLoggedIn = validateLoginMember(loginMember);
        memberService.updateMember(memberLoggedIn, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal Optional<LoginMember> loginMember) {
        final LoginMember memberLoggedIn = validateLoginMember(loginMember);
        memberService.deleteMember(memberLoggedIn);
        return ResponseEntity.noContent().build();
    }

    private LoginMember validateLoginMember(Optional<LoginMember> loginMember) {
        if (!loginMember.isPresent()) {
            throw new AuthorizationException();
        }
        return loginMember.get();
    }
}
