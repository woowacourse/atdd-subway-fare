package wooteco.subway.member.ui;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.ErrorResponse;

@RestControllerAdvice(assignableTypes = MemberController.class)
public class MemberControllerAdvice {
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("이미 가입된 이메일입니다."));
    }
}
