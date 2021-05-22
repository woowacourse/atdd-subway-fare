package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.net.URI;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal AuthMember authMember) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException();
        }
        MemberResponse member = memberService.findMember((LoginMember) authMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal AuthMember authMember, @RequestBody MemberRequest param) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException();
        }
        memberService.updateMember((LoginMember) authMember, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal AuthMember authMember) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException();
        }
        memberService.deleteMember((LoginMember) authMember);
        return ResponseEntity.noContent().build();
    }
}
