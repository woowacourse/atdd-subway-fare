package wooteco.subway.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wooteco.subway.auth.exception.InvalidTokenException;
import wooteco.subway.line.exception.line.LineException;
import wooteco.subway.line.exception.section.SectionException;
import wooteco.subway.member.exception.MemberException;
import wooteco.subway.station.exception.StationException;

@RestControllerAdvice
public class BaseControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StationException.class)
    public ResponseEntity<ErrorResponse> stationExceptionHandling(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(LineException.class)
    public ResponseEntity<ErrorResponse> lineExceptionHandling(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(SectionException.class)
    public ResponseEntity<ErrorResponse> sectionExceptionHandling(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> memberExceptionHandling(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> invalidTokenExceptionHandling(Exception e) {
        return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
}
