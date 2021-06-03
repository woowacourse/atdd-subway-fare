package wooteco.subway.exception.notfound;

public class InvalidDistanceException extends NotFoundException {
    public InvalidDistanceException() {
        super("계산 가능한 거리를 초과하였습니다.");
    }
}
