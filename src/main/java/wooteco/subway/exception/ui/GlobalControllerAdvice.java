package wooteco.subway.exception.ui;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ErrorDto;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler({SubwayException.class, DataAccessException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(final Exception e) {
        ErrorDto body = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity<ErrorDto> handleAuthorizationException(final Exception e) {
        ErrorDto body = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorDto> handleRuntimeException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
