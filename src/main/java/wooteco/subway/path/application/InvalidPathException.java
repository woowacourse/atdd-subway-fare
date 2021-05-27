package wooteco.subway.path.application;

public class InvalidPathException extends RuntimeException {
    private static final String ERROR_MESSAGE = "잘못된 경로입니다.";

    public InvalidPathException() {
        super(ERROR_MESSAGE);
    }
}
