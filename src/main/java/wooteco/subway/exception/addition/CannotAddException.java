package wooteco.subway.exception.addition;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.CustomException;

public class CannotAddException extends CustomException {
    public CannotAddException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
