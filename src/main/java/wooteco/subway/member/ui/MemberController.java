package wooteco.subway.member.ui;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.ChangeAgeRequest;
import wooteco.subway.member.dto.ChangeAgeResponse;
import wooteco.subway.member.dto.ChangePasswordRequest;
import wooteco.subway.member.dto.EmailExistsRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }

    @PostMapping("/members/exists")
    public ResponseEntity checkEmailExists(@Valid @RequestBody EmailExistsRequest emailExistsRequest) {
        memberService.checkEmailExists(emailExistsRequest.getEmail());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<ChangeAgeResponse> changeAge(@AuthenticationPrincipal LoginMember loginMember, @Valid @RequestBody ChangeAgeRequest request) {
        memberService.changeAge(loginMember, request);
        ChangeAgeResponse response = new ChangeAgeResponse(loginMember.getId(), request.getAge());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/members/me/pw")
    public ResponseEntity changePassword(@AuthenticationPrincipal LoginMember loginMember, @Valid @RequestBody ChangePasswordRequest request) {
        memberService.changePassword(loginMember, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }
}
