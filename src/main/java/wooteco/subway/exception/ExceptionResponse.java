package wooteco.subway.exception;

public class ExceptionResponse {
    private String error;
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(SubwayException exception) {
        this.error = exception.getError();
        this.message = exception.getMessage();
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
