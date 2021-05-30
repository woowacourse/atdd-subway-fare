package wooteco.subway.exception.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ErrorDto;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
    @ExceptionHandler({SubwayException.class, DataAccessException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(final Exception e) {
        log.error(e.getMessage(), e);
        ErrorDto body = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity<ErrorDto> handleAuthorizationException(final Exception e) {
        log.error(e.getMessage(), e);
        ErrorDto body = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorDto> handleRuntimeException(final Exception e) {
        log.error(e.getMessage(), e);
        ErrorDto errorDto = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
