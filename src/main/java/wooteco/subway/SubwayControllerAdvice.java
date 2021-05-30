package wooteco.subway;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.exception.invalid.InvalidAgeException;
import wooteco.subway.exception.invalid.InvalidDistanceException;
import wooteco.subway.exception.invalid.InvalidEmailException;
import wooteco.subway.exception.invalid.InvalidNameException;
import wooteco.subway.exception.invalid.InvalidPasswordException;
import wooteco.subway.exception.invalid.InvalidTokenException;
import wooteco.subway.exception.SubwayException;

@ControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandle(SubwayException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> invalidTokenExceptionHandle(InvalidTokenException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validExceptionHandle(MethodArgumentNotValidException e) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        if(message.contains("이메일")){
            ExceptionResponse exceptionResponse = new ExceptionResponse(new InvalidEmailException(message));
            return ResponseEntity.badRequest().body(exceptionResponse);
        }
        if(message.contains("비밀번호")){
            ExceptionResponse exceptionResponse = new ExceptionResponse(new InvalidPasswordException(message));
            return ResponseEntity.badRequest().body(exceptionResponse);
        }

        if(message.contains("나이")){
            ExceptionResponse exceptionResponse = new ExceptionResponse(new InvalidAgeException(message));
            return ResponseEntity.badRequest().body(exceptionResponse);
        }
        if(message.contains("구간 거리는 0")){
            ExceptionResponse exceptionResponse = new ExceptionResponse(new InvalidDistanceException(message));
            return ResponseEntity.badRequest().body(exceptionResponse);
        }
        ExceptionResponse exceptionResponse = new ExceptionResponse(new InvalidNameException(message));
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
