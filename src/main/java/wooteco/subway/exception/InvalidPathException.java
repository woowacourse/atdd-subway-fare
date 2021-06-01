package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPathException extends InvalidException {

    public InvalidPathException() {
        super("경로를 찾을 수 없습니다.");
    }
}
