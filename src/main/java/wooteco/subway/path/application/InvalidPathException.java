package wooteco.subway.path.application;

import wooteco.subway.exception.SubwayException;

public class InvalidPathException extends SubwayException {

    public InvalidPathException(String message) {
        super(message);
    }
}
