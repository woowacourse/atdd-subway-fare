package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.EmailCheckRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/members/exists")
public class EmailCheckerController {
    private MemberService memberService;

    public EmailCheckerController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity checkExistsMember(@RequestBody @Valid EmailCheckRequest email) {
        memberService.checkExistEmail(email);
        return ResponseEntity.noContent().build();
    }
}
