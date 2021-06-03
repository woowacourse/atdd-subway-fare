package wooteco.subway.path.application;

import wooteco.subway.exception.CommonException;

public class InvalidPathException extends CommonException {
    public InvalidPathException(String message) {
        super(message);
    }
}
