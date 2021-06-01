package wooteco.subway.exception;

public class InvalidPathException extends RuntimeException {

    private static final String MESSAGE = "잘못된 경로 입니다.";

    public InvalidPathException(String message) {
        super(message);
    }

    public InvalidPathException() {
        this(MESSAGE);
    }

}
