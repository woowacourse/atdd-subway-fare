package wooteco.subway.exception;

public class SubwayException extends RuntimeException {
    private final String error;
    private final String message;

    public SubwayException(String error, String message) {
        super(message);
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
