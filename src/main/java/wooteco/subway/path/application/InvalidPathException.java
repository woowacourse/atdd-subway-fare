package wooteco.subway.path.application;

public class InvalidPathException extends RuntimeException {

    private static final String ERROR_MESSAGE = "경로를 찾을 수 없습니다.";

    public InvalidPathException() {
        this(ERROR_MESSAGE);
    }

    public InvalidPathException(String message) {
        super(message);
    }
}
