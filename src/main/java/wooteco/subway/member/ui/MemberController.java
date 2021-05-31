package wooteco.subway.member.ui;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.validate.MemberValidator;

import javax.validation.Valid;
import java.net.URI;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @InitBinder("memberRequest")
    private void initBind(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new MemberValidator());
    }

    @GetMapping("/members/check-validation")
    @ApiOperation(value = "중복 이메일 조회", notes = "중복된 이메일을 검사한다.")
    public ResponseEntity findDuplicatedEmail(String email) {
        memberService.checkDuplicatedEmail(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/members")
    @ApiOperation(value = "회원 생성", notes = "회원을 생성한다.")
    public ResponseEntity createMember(@RequestBody @Valid MemberRequest memberRequest, BindingResult bindingResult) {
        MemberResponse member = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/me")
    @ApiOperation(value = "회원 조회", notes = "회원을 조회한다.")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    @ApiOperation(value = "회원 수정", notes = "회원을 수정한다.")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody MemberRequest param) {
        memberService.updateMember(loginMember, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    @ApiOperation(value = "회원 삭제", notes = "회원을 삭제한다.")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }
}
