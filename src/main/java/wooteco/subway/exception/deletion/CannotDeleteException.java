package wooteco.subway.exception.deletion;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.CustomException;

public class CannotDeleteException extends CustomException {

    public CannotDeleteException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
