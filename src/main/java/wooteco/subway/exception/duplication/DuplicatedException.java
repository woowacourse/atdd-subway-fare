package wooteco.subway.exception.duplication;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.CustomException;

public class DuplicatedException extends CustomException {

    public DuplicatedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
