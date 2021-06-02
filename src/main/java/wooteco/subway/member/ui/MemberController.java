package wooteco.subway.member.ui;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.ExceptionResponse;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/api/members/" + member.getId())).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(
        @AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(
        @AuthenticationPrincipal LoginMember loginMember, @RequestBody MemberRequest param) {
        memberService.updateMember(loginMember, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(
        @AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-validation")
    public ResponseEntity<Void> findExistMember(@RequestParam String email) {
        MemberResponse member = memberService.findMember(email);
        return ResponseEntity.ok().build();
    }

    // no email
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<FindFailEmailResponse> noEmailExceptionHandle() {
        return ResponseEntity.unprocessableEntity().body(
            new FindFailEmailResponse(ZonedDateTime.now(ZoneOffset.of("+09:00")).toString()
                , 422
                , "Unprocessable Entity"
                , ""
                , "/members/check-validation"));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ExceptionResponse> invalidAgeHandle() {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse("INVALID_AGE", "잘못된 나이입니다.")
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> nullHandle() {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse("INVALID_INPUT", "null 은 허용되지 않습니다.")
        );
    }
}
