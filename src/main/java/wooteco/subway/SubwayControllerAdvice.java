package wooteco.subway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.exception.dto.ErrorResponse;

@RestControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleRequestValidationException(final MethodArgumentNotValidException e) {
        final StringBuffer stringBuffer = new StringBuffer();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            stringBuffer.append(String.format("%s 검증 실패: %s %n", fieldName, errorMessage));
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(stringBuffer.toString()));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handleSubwayException(SubwayException e) {
        return ResponseEntity.status(e.getHttpStatus())
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnpredictableException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("서버 내부 예외가 발생했습니다. 담당자에게 문의하세요."));
    }
}
