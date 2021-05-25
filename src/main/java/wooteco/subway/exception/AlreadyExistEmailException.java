package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistEmailException extends RuntimeException {

    private static final String MESSAGE = "이미 사용된 이메일입니다.";

    public AlreadyExistEmailException() {
        super(MESSAGE);
    }
}