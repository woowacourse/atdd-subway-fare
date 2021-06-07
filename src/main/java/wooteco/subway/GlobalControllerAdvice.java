package wooteco.subway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.line.application.LineException;
import wooteco.subway.member.application.MemberException;
import wooteco.subway.path.application.PathException;
import wooteco.subway.station.application.StationException;
import wooteco.subway.station.ui.StationController;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalControllerAdvice {

    private static Logger logger = LoggerFactory.getLogger(StationController.class);

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizationException(AuthorizationException e) {
        String message = e.getMessage();

        ExceptionResponse exceptionResponse = new ExceptionResponse(message);
        logger.error(message);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }

    @ExceptionHandler({StationException.class, LineException.class, MemberException.class, PathException.class})
    public ResponseEntity<ExceptionResponse> handleException(RuntimeException e) {
        String message = e.getMessage();

        ExceptionResponse exceptionResponse = new ExceptionResponse(message);
        logger.error(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(BindingResult bindingResult) {
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError.getDefaultMessage();

        ExceptionResponse exceptionResponse = new ExceptionResponse(message);
        logger.error(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getMessage();

        ExceptionResponse exceptionResponse = new ExceptionResponse(message);
        logger.error(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnhandledException(Exception e) {
        logger.error(e.getMessage());
        logger.error(e.getLocalizedMessage());

        String message = "Oops!! There's unhandled exception";
        ExceptionResponse exceptionResponse = new ExceptionResponse(message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}
