package wooteco.subway.exception;

public class DuplicatedNameException extends RuntimeException {

    public DuplicatedNameException() {
    }

    public DuplicatedNameException(String message) {
        super(message);
    }
}
