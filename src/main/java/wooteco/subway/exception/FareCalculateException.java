package wooteco.subway.exception;

public class FareCalculateException extends SubwayException {

    public FareCalculateException() {
        super("요금 계산 중 문제가 발생했습니다.");
    }
}
