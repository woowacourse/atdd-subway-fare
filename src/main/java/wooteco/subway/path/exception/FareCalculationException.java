package wooteco.subway.path.exception;

public class FareCalculationException extends RuntimeException {
    private static final String ERROR_MESSAGE = "요금 계산에 실패했습니다.";

    public FareCalculationException() {
        super(ERROR_MESSAGE);
    }
}
