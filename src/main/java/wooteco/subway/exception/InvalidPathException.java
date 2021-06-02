package wooteco.subway.exception;

public class InvalidPathException extends RuntimeException {
    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        super(message);
    }
}
