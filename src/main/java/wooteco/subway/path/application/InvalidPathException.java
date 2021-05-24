package wooteco.subway.path.application;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        super(message);
    }
}
