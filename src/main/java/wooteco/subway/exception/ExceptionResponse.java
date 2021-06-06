package wooteco.subway.exception;

public class ExceptionResponse {
    private String message;
    private int status;

    public ExceptionResponse(String message, int status) {
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
