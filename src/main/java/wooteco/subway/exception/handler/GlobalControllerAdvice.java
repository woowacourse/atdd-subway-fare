package wooteco.subway.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.exception.BadRequestException;
import wooteco.subway.exception.dto.ErrorDto;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorDto> handleException(final Exception e) {
        ErrorDto body = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
