package wooteco.subway.exception.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.exception.DuplicateException;
import wooteco.subway.exception.InvalidException;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorDto> handleAuthorizationException(AuthorizationException e) {
        logger.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({DuplicateException.class, InvalidException.class, SubwayException.class})
    public ResponseEntity<ErrorDto> handleDuplicateException(RuntimeException e) {
        logger.warn(e.getMessage());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataAccessException.class, RuntimeException.class})
    public ResponseEntity<ErrorDto> handleSQLException(Exception e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(new ErrorDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
