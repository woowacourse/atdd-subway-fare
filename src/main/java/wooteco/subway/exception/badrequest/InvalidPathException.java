package wooteco.subway.exception.badrequest;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayException;

public class InvalidPathException extends SubwayException {

    private static final String INVALID_PATH_MESSAGE = "적절한 경로를 찾을 수 없습니다.";

    public InvalidPathException() {
        super(HttpStatus.BAD_REQUEST, INVALID_PATH_MESSAGE);
    }
}
