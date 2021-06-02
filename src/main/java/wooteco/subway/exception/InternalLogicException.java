package wooteco.subway.exception;

public class InternalLogicException extends RuntimeException {

    private static final String MESSAGE = "내부로직에 문제가 생겼습니다.";

    public InternalLogicException() {
        super(MESSAGE);
    }
}
