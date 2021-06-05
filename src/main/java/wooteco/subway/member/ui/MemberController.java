package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.infrastructure.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.User;
import wooteco.subway.member.dto.EmailCheckRequest;
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
    public ResponseEntity createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal User user) {
        MemberResponse memberResponse = memberService.findMember(user);
        return ResponseEntity.ok().body(memberResponse);
    }

    @PostMapping("/members/email-check")
    public ResponseEntity<Void> confirmEmail(@RequestBody EmailCheckRequest emailCheckRequest) {
        memberService.confirmEmailIsValid(emailCheckRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal User user,
                                                             @RequestBody MemberRequest param) {
        memberService.updateMember(user, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal User user) {
        memberService.deleteMember(user);
        return ResponseEntity.noContent().build();
    }
}
