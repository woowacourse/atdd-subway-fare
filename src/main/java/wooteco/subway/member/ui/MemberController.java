package wooteco.subway.member.ui;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.User;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(
        @AuthenticationPrincipal User user) {
        MemberResponse member = memberService.findMember(user);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(
        @AuthenticationPrincipal User user, @RequestBody MemberRequest param) {
        memberService.updateMember(user, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(
        @AuthenticationPrincipal User user) {
        memberService.deleteMember(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members/email-check")
    public ResponseEntity<MemberResponse> checkEmail(@RequestBody EmailRequest emailRequest) {
        memberService.checkEmail(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }
}
