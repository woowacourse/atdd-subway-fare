package wooteco.subway.exception;

public class BusinessException extends RuntimeException {

    private final ExceptionStatus exceptionStatus;

    public BusinessException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public int getStatus() {
        return exceptionStatus.getStatus();
    }
}
