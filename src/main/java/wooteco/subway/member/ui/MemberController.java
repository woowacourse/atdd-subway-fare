package wooteco.subway.member.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.MemberRelatedException;

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
    public ResponseEntity createMember(@RequestBody @Valid MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginUser loginUser) {
        MemberResponse member = memberService.findMember(loginUser);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal LoginUser loginUser, @RequestBody MemberRequest param) {
        memberService.updateMember(loginUser, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal LoginUser loginUser) {
        memberService.deleteMember(loginUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members/email-check")
    public ResponseEntity emailCheck(@RequestBody MemberRequest memberRequest) {
        memberService.checkEmail(memberRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MemberRelatedException.class)
    public ResponseEntity duplicateEmail(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }
}
