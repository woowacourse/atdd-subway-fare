package wooteco.subway.exception;

public class ExceptionResponse {
    private String error;
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(DuplicatedEmailException exception) {
        this.error = exception.getKey();
        this.message = exception.getMessage();
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
