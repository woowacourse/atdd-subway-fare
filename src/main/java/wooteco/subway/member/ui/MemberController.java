package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.dto.EmailExistsResponse;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/members")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<EmailExistsResponse> isExistsEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.isExistingEmail(email));
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId()))
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginUser loginUser) {
        MemberResponse member = memberService.findMember(loginUser);
        return ResponseEntity.ok()
                .body(member);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMemberOfMine(@AuthenticationPrincipal LoginUser loginUser,
                                                             @Valid @RequestBody MemberRequest param) {
        memberService.updateMember(loginUser, param);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal LoginUser loginUser) {
        memberService.deleteMember(loginUser);
        return ResponseEntity.noContent()
                .build();
    }
}
