package wooteco.subway.member.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.member.dto.EmailExistResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal AuthMember authMember) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException("유효하지 않은 토큰 정보입니다.");
        }
        MemberResponse member = memberService.findMember(authMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal AuthMember authMember, @RequestBody MemberRequest param) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException("유효하지 않은 토큰 정보입니다.");
        }
        memberService.updateMember(authMember, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal AuthMember authMember) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException("유효하지 않은 토큰 정보입니다.");
        }
        memberService.deleteMember(authMember);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/exists/{email}")
    public ResponseEntity<EmailExistResponse> isExistEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.isExistEmail(email));
    }
}
