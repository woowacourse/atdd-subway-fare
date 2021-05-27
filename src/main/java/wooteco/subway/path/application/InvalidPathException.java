package wooteco.subway.path.application;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException(String errorMessage) {
        super(errorMessage);
    }
}
