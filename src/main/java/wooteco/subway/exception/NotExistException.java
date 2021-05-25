package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotExistException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 데이터입니다.";

    public NotExistException() {
        super(MESSAGE);
    }
}
