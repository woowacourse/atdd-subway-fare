package wooteco.subway.exception.line;

public class InvalidDistanceException extends RuntimeException {
    private static final String MESSAGE = "잘못된 노선 구간 거리입니다.";

    public InvalidDistanceException() {
        super(MESSAGE);
    }
}
