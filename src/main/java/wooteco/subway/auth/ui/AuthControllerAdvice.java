package wooteco.subway.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.exception.ErrorResponse;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthControllerAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
