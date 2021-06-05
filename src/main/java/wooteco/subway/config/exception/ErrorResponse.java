package wooteco.subway.config.exception;

public class ErrorResponse {
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(Exception e) {
        this(e.getMessage());
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
