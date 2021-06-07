package wooteco.subway.exception;

public class ErrorResponse {
    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.errorMessage = message;
    }

    public String getMessage() {
        return errorMessage;
    }
}
