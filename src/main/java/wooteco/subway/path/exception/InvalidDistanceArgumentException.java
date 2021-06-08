package wooteco.subway.path.exception;

public class InvalidDistanceArgumentException extends PathDomainException {
    public static final String MESSAGE = "유효하지 않은 범위의 거리값 인자입니다.";

    public InvalidDistanceArgumentException() {
        super(MESSAGE);
    }
}
