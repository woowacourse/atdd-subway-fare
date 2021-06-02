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
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.domain.AuthenticationRequired;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.EmailRequest;
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
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        URI createdUri = URI.create("/members/" + member.getId());
        return ResponseEntity.created(createdUri)
            .build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationRequired LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<Void> updateMemberOfMine(@AuthenticationRequired LoginMember loginMember,
        @Valid @RequestBody MemberRequest param) {

        memberService.updateMember(loginMember, param);
        return ResponseEntity.noContent()
            .build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationRequired LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("/members/email-check")
    public ResponseEntity<Void> verifyUniqueEmail(@Valid @RequestBody EmailRequest emailRequest) {
        memberService.validateUniqueEmail(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }
}
