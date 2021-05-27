package wooteco.subway.exception.path;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPathException extends RuntimeException {
    private static final String MESSAGE = "경로를 조회할 수 없습니다.";

    public InvalidPathException() {
        super(MESSAGE);
    }
}
