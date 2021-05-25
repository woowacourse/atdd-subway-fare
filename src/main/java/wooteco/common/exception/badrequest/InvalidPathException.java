package wooteco.common.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidPathException extends BadRequestException {
    private static final String INVALID_PATH_ERROR_MESSAGE = "잘못된 구간 정보입니다.";

    public InvalidPathException() {
        super(INVALID_PATH_ERROR_MESSAGE);
    }
}
