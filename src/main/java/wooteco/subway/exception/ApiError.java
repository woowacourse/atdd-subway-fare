package wooteco.subway.exception;

public class ApiError {

    private String message;
    private int status;

    public ApiError(ErrorCode errorCode) {
        this(errorCode.getMessage(), errorCode.getStatus());
    }

    public ApiError(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
