package wooteco.subway;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import wooteco.subway.exception.ExceptionResponse;
import wooteco.subway.exception.InvalidAgeException;
import wooteco.subway.exception.InvalidEmailException;
import wooteco.subway.exception.InvalidInputException;
import wooteco.subway.exception.InvalidNameException;
import wooteco.subway.exception.InvalidPasswordException;
import wooteco.subway.exception.SubwayException;

@ControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandle(SubwayException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        return ResponseEntity.badRequest().body(exceptionResponse);
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
        ExceptionResponse exceptionResponse = new ExceptionResponse(new InvalidNameException(message));
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
