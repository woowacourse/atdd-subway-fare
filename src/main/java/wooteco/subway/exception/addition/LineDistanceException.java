package wooteco.subway.exception.addition;

public class LineDistanceException extends CannotAddException {
    private static final String MESSAGE = "잘못된 노선 구간 거리입니다.";

    public LineDistanceException() {
        super(MESSAGE);
    }
}
