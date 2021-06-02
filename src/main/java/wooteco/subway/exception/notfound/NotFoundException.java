package wooteco.subway.exception.notfound;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.CustomException;

public class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
