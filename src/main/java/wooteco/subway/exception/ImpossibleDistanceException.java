package wooteco.subway.exception;

public class ImpossibleDistanceException extends SubwayException {
    private static final String ERROR = "IMPOSSIBLE_DISTANCE";
    private static final String MESSAGE = "추가하고자 하는 구간의 거리가 기존 구간의 거리보다 깁니다.";

    public ImpossibleDistanceException() {
        super(ERROR, MESSAGE);
    }
}
