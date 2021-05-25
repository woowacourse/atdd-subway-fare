package wooteco.subway.exception;

public class InvalidPathException extends RuntimeException {

    private static final String INVALID_PATH_MESSAGE = "적절한 경로를 찾을 수 없습니다.";

    public InvalidPathException() {
        super(INVALID_PATH_MESSAGE);
    }
}
