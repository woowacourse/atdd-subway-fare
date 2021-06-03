package wooteco.subway.exception;

public class ExceptionResponse {
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    private String error;
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(SubwayException exception) {
        this.error = exception.getError();
        this.message = exception.getMessage();
    }

    public ExceptionResponse(AuthorizationException e) {
        this.error = INVALID_TOKEN;
        this.message = e.getMessage();
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
