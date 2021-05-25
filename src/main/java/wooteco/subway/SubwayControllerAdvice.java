package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.dto.ErrorResponse;
import wooteco.subway.member.exception.EmailAddressNotFoundException;

@RestControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(EmailAddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationFailure(EmailAddressNotFoundException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(e.getMessage()));
    }
}
