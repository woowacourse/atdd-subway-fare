package wooteco.subway.advice;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.subway.exception.dto.ErrorResponse;
import wooteco.subway.exception.duplicate.DuplicatedException;

@RestControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(Exception e) throws Exception{

        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw  e;
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage()));
    }
}
