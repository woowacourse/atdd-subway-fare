package wooteco.subway.member.ui;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.MustLoginPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.EmailRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @ApiOperation(value = "사용자 생성")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/me")
    @ApiOperation(value = "사용자 본인 정보 조회")
    public ResponseEntity<MemberResponse> findMemberOfMine(@MustLoginPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me")
    @ApiOperation(value = "사용자 본인 정보 수정")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@MustLoginPrincipal LoginMember loginMember,
                                                             @RequestBody MemberRequest param) {
        memberService.updateMember(loginMember, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    @ApiOperation(value = "사용자 본인 삭제")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@MustLoginPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email-check")
    @ApiOperation(value = "이메일 중복 조회")
    public ResponseEntity<Void> checkEmailDuplication(@RequestParam("email") EmailRequest request) {
        memberService.checkEmail(request);
        return ResponseEntity.ok().build();
    }
}
